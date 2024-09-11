package com.gold.resource.auth.filter;

import com.gold.resource.AletheiaUser;
import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.auth.grpc.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final AuthServiceClient authServiceClient;

    /**
     * Access Token(JWT) 검사 및 인증 정보 설정
     * gRPC를 사용하여 인증 서버와 통신, 사용자 인증 여부 및 정보를 받아온 뒤 인증 정보 설정
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("*** REQUEST URL: {}", request.getRequestURL());

        // 헤더에서 Access Token 추출
        String token = resolveToken(request);

        if (token != null) {
            // JWT -> Access Token 객체
            AletheiaUser user = authServiceClient.getAuthentication(token);
            setAuthentication(user);
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

    /**
     * 인증 정보 설정
     * @param user Custom UserDetails 객체
     */
    private void setAuthentication(AletheiaUser user) {
        Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        CustomUserDetails userDetails = new CustomUserDetails(user.getId(), user.getUsername(), authorities); // PK, ID, 역할
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
