package com.gold.resource.sales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SalesStatus {
    ORDER_COMPLETED("주문 완료"),
    PAYMENT_COMPLETED("송금 완료"),
    RECEIPT_COMPLETED ("수령 완료");

    private final String description;

    public static SalesStatus fromString(String status) {
        for (SalesStatus salesStatus : SalesStatus.values()) {
            if (salesStatus.name().equals(status)) {
                return salesStatus;
            }
        }
        throw new IllegalArgumentException(status + "은/는 지원하지 않는 판매 상태입니다.");
    }
}
