package com.onetool.server.global.config;


import com.onetool.server.global.auth.CustomAccessDeniedHandler;
import com.onetool.server.global.auth.CustomAuthenticationEntryPoint;
import com.onetool.server.global.auth.filter.JwtAuthFilter;
import com.onetool.server.global.auth.jwt.JwtUtil;
import com.onetool.server.global.auth.login.handler.OAuth2LoginFailureHandler;
import com.onetool.server.global.auth.login.handler.OAuth2LoginSuccessHandler;
import com.onetool.server.global.auth.login.service.CustomOAuth2UserService;
import com.onetool.server.global.auth.login.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    private static final String[] AUTH_WHITELIST = {
            "/", "/users/**", "/login/**", "/blueprint/**", "/actuator/**", "/silent-refresh", "/payments/**"
            ,"/swagger-ui/**","/v3/api-docs/**","/swagger-ui.html", "/chat/**", "/chatting/**", "/ws/**", "/ws-stomp/**"
    };

    @Bean
    public SecurityContextRepository securityContextRepository(){
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .anonymous(AbstractHttpConfigurer::disable)
                .securityContext((securityContext) -> {
                    securityContext.securityContextRepository(securityContextRepository());
                })
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/orders").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/payments/deposit").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(auth -> {
                    auth.successHandler(oAuth2LoginSuccessHandler)
                            .failureHandler(oAuth2LoginFailureHandler)
                            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService));
                });

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("^https?:\\/\\/linklinklink~~.com$");
        configuration.addAllowedOrigin("http://www.onetool.co.kr");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://onetool.co.kr");
        configuration.addAllowedOrigin("http://api.onetool.co.kr");
        configuration.addAllowedOrigin("https://api.onetool.co.kr");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://accounts.google.com");
        configuration.addAllowedOrigin("https://api.tosspayments.com");
        configuration.addAllowedOrigin("https://frontend-beta-two-45.vercel.app");
        configuration.addAllowedOrigin("http://frontend-beta-two-45.vercel.app");
        configuration.addAllowedOrigin("https://onetoolcad.vercel.app");
        configuration.addAllowedOrigin("http://onetoolcad.vercel.app");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
