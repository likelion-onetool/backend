package com.onetool.server.global.oauth2.handler;

import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.oauth2.CustomOAuth2User;
import com.onetool.server.member.MemberRepository;
import com.onetool.server.member.UserRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if(oAuth2User.getRole() == UserRole.ROLE_GUEST) {
                MemberAuthContext memberAuthContext = MemberAuthContext.builder()
                        .email(oAuth2User.getEmail())
                        .role(oAuth2User.getRole().name())
                        .build();
                String accessToken = jwtUtil.create(memberAuthContext);
                response.addHeader("Authorization", "Bearer " + accessToken);
                response.sendRedirect("oauth2/sign-up");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws  IOException {
        MemberAuthContext memberAuthContext = MemberAuthContext.builder()
                .email(oAuth2User.getEmail())
                .role(oAuth2User.getRole().name())
                .build();
        String accessToken = jwtUtil.create(memberAuthContext);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
