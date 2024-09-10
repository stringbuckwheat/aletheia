package com.gold.auth.auth.filter;

import com.gold.auth.auth.service.TokenProvider;
import com.gold.auth.auth.token.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;

    /**
     * Access Token(JWT) 검사 및 인증 정보 설정
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ExpiredJwtException Access Token 유효시간 만료 시
     * @throws JwtException MalformedJwtException 등 유효하지 않은 JWT가 들어왔을 떄
     * @throws NumberFormatException Jwt Claim 내부 정보 형변환 과정에서 발생 가능
     *
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("*** JWT FILTER: REQUEST URL: {}", request.getRequestURL());

        // 헤더에서 Access Token 추출
        String token = resolveToken(request);

        if (token != null) {
            // JWT 토큰을 AccessToken 객체로 변환
            AccessToken accessToken = tokenProvider.convertAccessToken(token);
            log.info("access token: {}", accessToken);

            // SecurityContext에 인증 정보 설정
            setAuthenticationFromClaims(accessToken.getData());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 헤더에서 token 추출
     *
     * @param request
     * @return Authorization 헤더에 Bearer로 시작하는 토큰이 포함되어 왔을 때, 'Bearer' 떼고 반환
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length()).trim();
        }

        return null;
    }

    private void setAuthenticationFromClaims(Claims claims) {
        log.info("claims: {}", claims);

        Long userId = Long.valueOf(String.valueOf(claims.get("aud")));
        String username = claims.getSubject();
        String role = String.valueOf(claims.get("role"));

        log.info("userId, username, role: {}, {}, {}", userId, username, role);

        tokenProvider.setAuthentication(userId, username, role);
    }
}