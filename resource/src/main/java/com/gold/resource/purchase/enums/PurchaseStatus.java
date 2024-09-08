package com.gold.resource.purchase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseStatus {
    ORDER_COMPLETED("주문 완료"),
    PAYMENT_COMPLETED("입금 완료"),
    SHIPMENT_COMPLETED("발송 완료");

    private final String description;
}
