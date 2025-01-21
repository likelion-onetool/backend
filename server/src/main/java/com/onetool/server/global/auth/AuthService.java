package com.onetool.server.global.auth;

import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.InvalidTokenException;
import com.onetool.server.global.redis.service.TokenRedisService;
import com.onetool.server.member.dto.MemberLoginResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final TokenRedisService tokenRedisService;

    public void validate(String requestAccessTokenInHeader) {
        String requestAccessToken = requestAccessTokenInHeader.substring(7);
        if(!jwtUtil.validateToken(requestAccessToken)) {
            throw new InvalidTokenException();
        }
    }

    @Transactional
    public void saveRefreshToken(String memberId, String refreshToken) {
        tokenRedisService.setValuesWithTimeout(
                memberId,
                refreshToken,
                jwtUtil.getRefreshTokenExpirationMillis()
        );
    }

    @Transactional
    public Map<String, String> reissue(String requestRefreshTokenInHeader) {

        log.info("reissue() - 진입");

        Authentication authentication = jwtUtil.getAuthenticationByRefreshToken(requestRefreshTokenInHeader);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        MemberAuthContext memberAuthContext = principalDetails.getContext();

        String memberIdString = memberAuthContext.getId().toString();
        String refreshTokenInRedis = tokenRedisService.getValues(memberIdString);

        log.info("reissue() - refreshTokenInRedis: {}", refreshTokenInRedis);

        if(refreshTokenInRedis.equals("false")) {
            return null;
        }

        if(!jwtUtil.validateToken(refreshTokenInRedis)) {
            tokenRedisService.deleteValues(memberIdString);
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        tokenRedisService.deleteValues(memberIdString);
        return jwtUtil.createTokens(memberAuthContext);
    }

    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
}
