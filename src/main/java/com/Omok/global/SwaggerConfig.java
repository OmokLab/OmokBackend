package com.Omok.global;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "My API", version = "1.0", description = "API Documentation"),
        security = @SecurityRequirement(name = "BearerAuth") // SecurityRequirement를 전역으로 추가
)
@SecurityScheme(
        name = "BearerAuth", // Security Scheme 이름
        type = SecuritySchemeType.HTTP, // 인증 유형은 HTTP
        scheme = "bearer", // Bearer 토큰 방식 사용
        bearerFormat = "JWT" // JWT 형식 지정 (선택 사항)
)
public class SwaggerConfig {
}
