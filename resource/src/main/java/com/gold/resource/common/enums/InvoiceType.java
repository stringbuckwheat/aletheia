package com.gold.resource.common.enums;

import lombok.AllArgsConstructor;

/**
 * 송장(목록) 요청 시 안전한 타입을 제공하기 위한 enum 클래스
 */
@AllArgsConstructor
public enum InvoiceType {
    INVOICE("invoice"),
    PURCHASE("purchase"),
    SALES("sales");

    private String domain;

    public String get() {
        return domain;
    }
}
