package com.gold.auth.user.service;

import com.gold.auth.common.error.exception.HasSameUsernameException;
import com.gold.auth.common.error.exception.RefreshTokenException;
import com.gold.auth.user.dto.AuthTokens;
import com.gold.auth.user.dto.LoginRequest;
import com.gold.auth.user.dto.RegisterRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public interface UserPublicService {
    /**
     * 아이디 중복 확인
     *
     * @param username 중복 검사를 수행할 사용자 이름
     * @return 중복되지 않은 경우 사용자 이름을 반환
     * @throws HasSameUsernameException 사용자 이름이 이미 사용 중인 경우 예외
     */
    @Transactional(readOnly = true)
    String hasSameUsername(String username);

    /**
     * 회원 가입
     *
     * @param register 등록할 사용자 정보
     * @return 사용자 등록 후 발급된 액세스/리프레쉬 토큰
     */
    @Transactional
    AuthTokens register(RegisterRequest register);

    /**
     * 로그인
     *
     * @param loginRequest 로그인 요청 정보
     * @return 로그인 성공 후 발급된 인증 토큰
     * @throws UsernameNotFoundException 해당 유저가 존재하지 않는 경우
     * @throws BadCredentialsException 비밀번호가 일치하지 않는 경우
     */
    AuthTokens login(LoginRequest loginRequest);

    /**
     * 리프레시 토큰을 사용하여 액세스 토큰 재발급
     *
     * @param refreshToken 리프레시 토큰
     * @return 새로운 액세스 토큰
     */
    @Transactional(readOnly = true)
    String reissueToken(String refreshToken);

    /**
     * 로그아웃, 리프레시 토큰 삭제
     *
     * @param refreshToken 리프레시 토큰
     * @throws RefreshTokenException 리프레시 토큰이 없거나, 유효하지 않은 경우
     */

    void logOut(String refreshToken);
}
