package com.gold.auth.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.common.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class AuthExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

    /**
     * 인증/인가 필터에서 발생한 예외처리용 필터
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 인증/인가 필터에서 발생한 예외처리
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // Access Token 만료
            log.info("Access Token expired: {}, Message: {}, requestUrl: {}",
                    request.getHeader("Authorization"), e.getMessage(), request.getRequestURL());

            setErrorResponse(response, ErrorMessage.ACCESS_TOKEN_EXPIRED.getMessage());
        } catch (JwtException | NumberFormatException e) {
            // 유효하지 않은 JWT 토큰 도착
            // NumberFormatException: Claim의 정보 형변환 과정에서의 오류
            log.warn("{} by invalid access token: {}, Message: {}, requestUrl: {}",
                    e.getClass().getSimpleName(),
                    request.getHeader("Authorization"), e.getMessage(), request.getRequestURL());

            setErrorResponse(response, ErrorMessage.INVALID_ACCESS_TOKEN.getMessage());
        } catch (Exception e) {
            // 기타
            log.error("Unexpected error occurred with message: {}, requestUrl: {}", e.getMessage(), request.getRequestURL(), e);

            setErrorResponse(response, ErrorMessage.UNEXPECTED_ERROR_OCCUR.getMessage());
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
        ErrorResponse errorResponse = new ErrorResponse(message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(CONTENT_TYPE);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}