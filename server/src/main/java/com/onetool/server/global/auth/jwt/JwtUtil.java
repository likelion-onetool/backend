package com.onetool.server.global.auth.jwt;

import com.onetool.server.global.auth.AuthorizationProvider;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.auth.login.service.CustomUserDetailsService;
import com.onetool.server.global.redis.domain.Token;
import com.onetool.server.global.redis.repository.TokenRepository;
import com.onetool.server.global.redis.service.TokenBlackListRedisService;
import com.onetool.server.global.redis.service.TokenRedisService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@Setter
public class JwtUtil implements AuthorizationProvider {

    private static final String USER_NAME = "name";
    private static final String USER_ROLE = "role";

    private final SecretKey key;
    private final Long expirationMilliSec;
    @Getter
    private final Long refreshTokenExpirationMillis;

    private final TokenRedisService tokenRedisService;
    private final TokenBlackListRedisService tokenBlackListRedisService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtUtil(
            @Value("${onetool.jwt.secrekey}") String secretKey,
            @Value("${onetool.jwt.expiration_time}") Long expirationMilliSec,
            @Value("${onetool.jwt.refresh.expiration_time}") Long refreshTokenExpirationMillis,
            TokenBlackListRedisService tokenBlackListRedisService,
            CustomUserDetailsService customUserDetailsService,
            TokenRedisService tokenRedisService
            ) {
        byte[] keyBytes = Base64.getDecoder()
                .decode(secretKey.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
        this.expirationMilliSec = expirationMilliSec;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.tokenBlackListRedisService = tokenBlackListRedisService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenRedisService = tokenRedisService;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public Map<String, String> createTokens(MemberAuthContext context) {
        String accessToken = createAccessToken(context);
        String refreshToken = createRefreshToken(context);
        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Override
    public Claims parseClaims(String token) {
        try {
            log.info("parseClaims token: {}", token);
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            log.info("validateToken token: {}", token);
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            if(tokenBlackListRedisService.hasKey(token)) {
                return false;
            }
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    @Override
    public Long getUserId(String token) {
        Double db = parseClaims(token).get("memberId", Double.class);
        return db.longValue();
    }

    private String createRefreshToken(MemberAuthContext context) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", context.getId());
        claims.put("email", context.getEmail());
        claims.put("role", context.getRole());

        Instant now = Instant.now();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(refreshTokenExpirationMillis)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("redis set refresh token: {} : {}", context.getEmail(), refreshToken);
        tokenRedisService.setValuesWithTimeout(context.getEmail(), refreshToken, refreshTokenExpirationMillis);
        return refreshToken;
    }

    private String createAccessToken(MemberAuthContext context) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", context.getId());
        claims.put("email", context.getEmail());
        claims.put("role", context.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expirationMilliSec);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        String id = getUserId(token).toString();
        PrincipalDetails principalDetails = customUserDetailsService.loadUserByUsername(id);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    @Override
    public Authentication getAuthenticationByRefreshToken(String refreshToken) {
        String id = getUserId(refreshToken).toString();
        PrincipalDetails principalDetails = customUserDetailsService.loadUserByUsername(id);

        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    public Long getExpirationMilliSec(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
