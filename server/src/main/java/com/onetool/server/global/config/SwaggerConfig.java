package com.onetool.server.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0") // 버전
                .title("Onetool API") // 이름
                .description("3D CAD 도면 거래 플랫폼 API"); // 설명

        // SecurityScheme 정의 (JWT 인증 방식 설정)
        SecurityScheme securityScheme = new SecurityScheme()
                .name("bearerAuth") // Swagger에서 보이는 이름
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")) // JWT 인증 추가
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearerAuth", securityScheme));
    }
}
