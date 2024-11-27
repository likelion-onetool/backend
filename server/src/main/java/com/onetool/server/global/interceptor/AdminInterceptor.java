package com.onetool.server.global.interceptor;

import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.api.member.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AdminInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tokenWithBearer = request.getHeader("Authorization");
        String token = tokenWithBearer.substring(7);
        String role = jwtUtil.parseClaims(token).get("role", String.class);
        if(role.equals(UserRole.ROLE_ADMIN.name())) {
            return true;
        }
        return false;
    }
}