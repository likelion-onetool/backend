package com.onetool.server.global.auth.filter;

import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.PrincipalDetails;
import com.onetool.server.global.auth.login.service.CustomUserDetailsService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    @Resource
    private CustomUserDetailsService customUserDetailsService;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private SecurityContextRepository securityContextRepository;

    private static final String[] SHOULD_NOT_FILTER_URI_LIST = {"/actuator/**"};


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(SHOULD_NOT_FILTER_URI_LIST)
                .anyMatch(uri -> new AntPathMatcher().match(uri, request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            PrincipalDetails principalDetails = checkAccessTokenValid(token);
            if (principalDetails != null) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(principalDetails, token, principalDetails.getAuthorities());
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(securityContext);
                securityContextRepository.saveContext(securityContext, request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private PrincipalDetails checkAccessTokenValid(String accessToken) {
        String id = jwtUtil.getUserId(accessToken).toString();
        PrincipalDetails principalDetails = customUserDetailsService.saveUserInSecurityContext(id, accessToken);
        if(jwtUtil.validateToken(accessToken)) {
            return principalDetails;
        }
        return null;
    }
}
