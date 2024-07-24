package com.onetool.server.global.auth.jwt;

import com.onetool.server.global.auth.AuthorizationProvider;
import com.onetool.server.global.auth.MemberAuthContext;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil implements AuthorizationProvider {

    private static final String USER_NAME = "name";
    private static final String USER_ROLE = "role";

    private final Key key;
    private final Long expirationMilliSec;

    public JwtUtil(
            @Value("${onetool.jwt.secrekey}") String secretKey,
            @Value("${onetool.jwt.expiration_time}") Long expirationMilliSec
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMilliSec = expirationMilliSec;
    }

    @Override
    public String create(MemberAuthContext context) {
        Claims claims = Jwts.claims();
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
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
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
        return parseClaims(token).get("memberId", Long.class);
    }
}
