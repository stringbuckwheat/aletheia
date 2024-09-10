package com.gold.auth.user.service;

import com.gold.auth.user.dto.PasswordRequest;
import com.gold.auth.user.dto.UserResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

public interface UserAuthService {
    /**
     * 사용자 ID(PK)를 기반으로 사용자 상세 정보 조회
     * 삭제 일자가 없는 경우에만(== 탈퇴하지 않은 경우) 조회
     *
     * @param userId 조회할 사용자의 ID
     * @return 조회된 사용자의 정보를 담은 {@link UserResponse} 객체
     * @throws NoSuchElementException 사용자가 존재하지 않거나 탈퇴한 경우
     */
    @Transactional(readOnly = true)
    UserResponse getDetail(Long userId);

    /**
     * 사용자 비밀번호 수정
     * 삭제 일자가 없는 경우에만(== 탈퇴하지 않은 경우) 조회
     *
     * @param passwordRequest 이전 비밀번호, 변경할 비밀번호
     * @param userId 사용자 ID (AuthenticationPricipal을 사용해 추출)
     * @throws NoSuchElementException 사용자가 존재하지 않거나 탈퇴한 경우
     * @throws BadCredentialsException 이전 비밀번호와 저장된 비밀번호가 일치하지 않는 경우
     * @throws IllegalArgumentException 직전에 사용하던 비밀번호와 수정하려는 비밀번호가 동일한 경우
     */
    @Transactional
    void updatePassword(PasswordRequest passwordRequest, Long userId);

    /**
     * 회원 탈퇴
     *
     * 사용자 삭제 전 다음 작업 수행:
     * 1) RefreshToken을 사용하여 로그아웃
     * 2) 사용자 ID에 해당하는 (탈퇴하지 않은) 사용자 존재 여부 확인 및 예외 발생
     * 3) 자
     *
     * @param userId 사용자 ID
     * @param refreshToken 사용자의 Refresh Token
     * @throws NoSuchElementException 사용자가 존재하지 않는 경우
     */
    @Transactional
    void delete(Long userId, String refreshToken);
}
