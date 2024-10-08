package com.gold.auth.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 실패 시 클라이언트에 JSON 형식 예외 응답 반환
 */
@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Authentication failed in URI: {}, with message: {}", request.getRequestURI(), authException.getMessage(), authException);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE);

        response.getWriter().write(objectMapper.writeValueAsString(new ErrorResponse(ErrorMessage.PLEASE_LOGIN.getMessage())));
    }
}
