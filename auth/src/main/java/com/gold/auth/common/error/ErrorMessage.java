package com.gold.auth.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 예외 메시지
 */
@AllArgsConstructor
@Getter
public enum ErrorMessage {
    WRONG_USERNAME("아이디를 확인해주세요"),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다"),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다. 다시 로그인해주세요"),
    ACCESS_TOKEN_EXPIRED("액세스 토큰이 만료되었습니다. 재발급해주세요"),
    REFRESH_TOKEN_EXPIRED("리프레쉬 토큰이 만료되었습니다. 다시 로그인해주세요."),
    UNEXPECTED_ERROR_OCCUR("잠시 후 다시 시도해 주시기 바랍니다."),
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레쉬 토큰입니다. 다시 로그인해주세요."),
    NO_REFRESH_TOKEN("리프레쉬 토큰이 존재하지 않습니다."), // 요청에 refresh token이 오지 않은 경우
    PLEASE_LOGIN("로그인이 필요한 엔드포인트입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다."),
    SAME_PASSWORD("직전과 똑같은 비밀번호를 사용할 수 없습니다."),
    REQUEST_VALIDATION_FAILED("입력 정보를 확인해주세요");

    String message;
}
