package com.gold.auth.user.service;

import com.gold.auth.common.error.ErrorMessage;
import com.gold.auth.user.dto.PasswordRequest;
import com.gold.auth.user.dto.UserResponse;
import com.gold.auth.user.model.User;
import com.gold.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserPublicService userPublicService;

    // 사용자 상세 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserResponse getDetail(Long userId) {
        // PK, 삭제 일자가 없는 유저 조회
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        return new UserResponse(user);
    }

    // 비밀번호 수정
    @Override
    @Transactional
    public void updatePassword(PasswordRequest passwordRequest, Long userId) {
        // PK, 삭제 일자가 없는 유저 조회
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        if (!encoder.matches(passwordRequest.getPrevPassword(), user.getPassword())) {
            // 이전 비밀번호와 저장된 비밀번호 일치 여부 확인
            throw new BadCredentialsException(ErrorMessage.WRONG_PASSWORD.getMessage());
        } else if (passwordRequest.getPrevPassword().equals(passwordRequest.getNewPassword())) {
            // 직전 사용한 비밀번호 사용 불가
            throw new IllegalArgumentException(ErrorMessage.SAME_PASSWORD.getMessage());
        }

        user.updatePassword(encoder.encode(passwordRequest.getNewPassword()));
    }

    // 회원 탈퇴
    @Override
    @Transactional
    public void delete(Long userId, String refreshToken) {
        // 로그아웃, Refresh Token 삭제
        userPublicService.logOut(refreshToken);

        // PK, 삭제 일자가 없는 유저 조회
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.USER_NOT_FOUND.getMessage()));

        // deleteAt 컬럼에 현재 시각 저장
        user.delete();
    }
}
