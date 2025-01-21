package com.onetool.server.global.auth.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onetool.server.global.auth.MemberAuthContext;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.api.member.dto.MemberLoginResponse;
import com.onetool.server.api.member.repository.MemberRepository;
import com.onetool.server.api.member.enums.UserRole;
import com.onetool.server.global.auth.login.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공");
        try {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            MemberAuthContext member = principalDetails.getContext();

            if(Objects.equals(member.getRole(), UserRole.ROLE_GUEST.name())) {
                MemberAuthContext memberAuthContext = MemberAuthContext.builder()
                        .email(member.getEmail())
                        .role(member.getRole())
                        .build();

                Map<String, String> tokens = jwtUtil.createTokens(memberAuthContext);
                MemberLoginResponse tokenResponse = MemberLoginResponse.builder()
                                .accessToken("Bearer " + tokens.get("accessToken"))
                                .build();
                String result = objectMapper.writeValueAsString(tokenResponse);

                response.getWriter().write(result);
                response.sendRedirect("oauth2/sign-up");
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                loginSuccess(response, member);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void loginSuccess(HttpServletResponse response, MemberAuthContext context) throws  IOException {
        MemberAuthContext memberAuthContext = MemberAuthContext.builder()
                .id(context.getId())
                .email(context.getEmail())
                .role(context.getRole())
                .build();

        Map<String, String> tokens = jwtUtil.createTokens(memberAuthContext);
        MemberLoginResponse tokenResponse = MemberLoginResponse.builder()
                .accessToken("Bearer " + tokens.get("accessToken"))
                .build();
        String result = objectMapper.writeValueAsString(tokenResponse);

        response.getWriter().write(result);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}