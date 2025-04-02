package com.onetool.server.api.member.business;

import com.onetool.server.api.member.domain.Member;
import com.onetool.server.api.member.dto.request.LoginRequest;
import com.onetool.server.api.member.service.MemberService;
import com.onetool.server.global.annotation.Business;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.exception.ApiResponse;
import com.onetool.server.global.new_exception.exception.ApiException;
import com.onetool.server.global.new_exception.exception.error.LoginErrorCode;
import com.onetool.server.global.redis.service.TokenBlackListRedisService;
import com.onetool.server.global.redis.service.TokenRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@Business
@RequiredArgsConstructor
@Slf4j
public class MemberLoginBusiness {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    private final TokenRedisService tokenRedisService;
    private final TokenBlackListRedisService tokenBlackListRedisService;
    private final MemberService memberService;

    public Map<String, String> login(LoginRequest request) {
        Member member = memberService.findOne(request.getEmail());

        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            log.error("비밀번호 불일치: {}", encoder.encode(member.getPassword()));
            throw new ApiException(LoginErrorCode.INVALID_PASSWORD);
        }
        log.info("fuck");
        System.out.println(member.getName());
        System.out.println(member.getRole());
        MemberAuthContext context = MemberAuthContext.from(member);
        return jwtUtil.createTokens(context);
    }

    public ApiResponse<String> logout(String accessToken, String email) {
        Long expiration = jwtUtil.getExpirationMilliSec(accessToken);
        tokenBlackListRedisService.setBlackList(accessToken, expiration);

        if (!tokenRedisService.hasKey(email)) {
            throw new ApiException(LoginErrorCode.ILLEGAL_LOGOUT_USER);
        }
        tokenRedisService.deleteValues(email);

        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }
}
