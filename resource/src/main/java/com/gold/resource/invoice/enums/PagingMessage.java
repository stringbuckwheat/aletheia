package com.gold.resource.invoice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PagingMessage {
    SUCCESS("주문 내역 조회에 성공했습니다"),
    EMPTY("선택한 기간과 조건에 맞는 데이터가 존재하지 않습니다.");

    private String message;
}
