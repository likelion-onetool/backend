package com.onetool.server.api.member.controller;

import com.onetool.server.api.member.business.MemberLoginBusiness;
import com.onetool.server.api.member.dto.request.LoginRequest;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.exception.ApiResponse;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberLoginController {

    private final MemberLoginBusiness memberLoginBusiness;
    private final JwtUtil jwtUtil;

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

    @DeleteMapping("/logout")
    public ApiResponse<String> logout(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            HttpServletRequest servletRequest
    ) {
        String accessToken = jwtUtil.resolveToken(servletRequest);
        return memberLoginBusiness.logout(accessToken, principalDetails.getUsername());
    }
}
