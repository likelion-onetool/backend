package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.business.MemberLoginBusiness;
import com.onetool.server.api.member.dto.request.LoginRequest;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.onetool.server.global.util.CookieUtil.createRefreshTokenCookie;

@Tag(name = "회원 - 로그인/로그아웃", description = "로그인 및 로그아웃 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class MemberLoginController {

    private final MemberLoginBusiness memberLoginBusiness;
    private final JwtUtil jwtUtil;

    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인하고 Access/Refresh 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ApiResponse<String> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse servletResponse
    ) {
        Map<String, String> tokens = memberLoginBusiness.login(request);
        ResponseCookie refreshTokenCookie = createRefreshTokenCookie(tokens.get("refreshToken"));
        servletResponse.setHeader("Set-Cookie", refreshTokenCookie.toString());
        return ApiResponse.onSuccess(tokens.get("accessToken"));
    }

    @Operation(summary = "로그아웃 API", description = "현재 로그인된 사용자를 로그아웃 처리하고 토큰을 만료시킵니다.")
    @DeleteMapping("/logout")
    public ApiResponse<String> logout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            HttpServletRequest servletRequest
    ) {
        String accessToken = jwtUtil.resolveToken(servletRequest);
        return memberLoginBusiness.logout(accessToken, principalDetails.getUsername());
    }
}
