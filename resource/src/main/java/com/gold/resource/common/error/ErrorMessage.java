package com.gold.resource.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    SALES_NOT_FOUND("해당 판매 주문을 찾을 수 없습니다."),
    PLEASE_LOGIN("인증이 필요한 엔드포인트입니다. 로그인해주세요"),
    NOT_YOUR_INVOICE("해당 주문에 대한 접근 권한이 없습니다."),
    PURCHASE_NOT_FOUND("해당 구매 주문을 찾을 수 없습니다."),
    UNEXPECTED_ERROR_OCCURRED("잠시 후 다시 시도해 주시기 바랍니다.");

    private String message;
}
