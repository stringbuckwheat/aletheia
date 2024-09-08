package com.gold.resource.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gold.resource.common.error.ErrorMessage;
import com.gold.resource.common.error.ErrorResponse;
import io.grpc.StatusRuntimeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtFilter에서 발생하는 예외처리용 필터
 */
@Component
@Slf4j
public class AuthExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 인증/인가 필터에서 발생한 예외처리
            filterChain.doFilter(request, response);
        } catch (StatusRuntimeException e) {
            // Access Token 만료
            setErrorResponse(response, e.getStatus().getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            setErrorResponse(response, ErrorMessage.UNEXPECTED_ERROR_OCCURRED.getMessage());
        }
    }

    /**
     * 예외 발생 시 응답 객체를 만드는 메소드
     * (401 UNAUTHORIZED 리턴)
     *
     * @param response 응답
     * @param message 예외 메시지
     * @throws IOException
     */
    public void setErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE);

        ErrorResponse errorResponse = new ErrorResponse(message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}