package com.onetool.server.global.redis.controller;

import com.onetool.server.global.auth.AuthService;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "인증/토큰", description = "토큰 유효성 검사 및 재발급 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TokenController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

    @Operation(summary = "Access Token 유효성 검사 API", description = "Request Header의 Access Token의 유효성을 검사합니다.")
    @PostMapping("/auth/validate")
    public void validate(@RequestHeader("Authorization") String requestAccessToken) {
        authService.validate(requestAccessToken);
    }

    @Operation(summary = "토큰 재발급 API (Silent Refresh)", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 재발급합니다.")
    @PostMapping("/silent-refresh")
    public ApiResponse<?> reissue(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse servletResponse
    ) {
        log.info("reissue token: {}", refreshToken);
        Map<String, String> tokens = authService.reissue(refreshToken);
        log.info("reissued token: {}", tokens);

        if (tokens != null) {
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokens.get("refreshToken"))
                    .maxAge(860000)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            servletResponse.setHeader("Set-Cookie", responseCookie.toString());
            return ApiResponse.onSuccess(tokens.get("accessToken"));
        } else {
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            return ApiResponse.onFailure("401",
                    "기존 refreshToken이 유효하지 않습니다.",
                    null
                    );
        }
    }
}
