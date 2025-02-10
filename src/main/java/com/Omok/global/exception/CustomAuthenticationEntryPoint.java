package com.Omok.global.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // 응답의 Content-Type을 JSON으로 설정
        response.setContentType("application/json;charset=UTF-8");
        // HTTP 상태 코드 401 (Unauthorized) 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 클라이언트에 전달할 JSON 응답 구성
        String jsonResponse = String.format(
                "{\"error\": \"unauthorized\", \"message\": \"%s\"}",
                authException.getMessage()
        );

        // 응답 작성
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
        writer.close();
    }
}
