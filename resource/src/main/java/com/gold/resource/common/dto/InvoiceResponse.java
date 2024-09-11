package com.gold.resource.common.dto;

import com.gold.resource.purchase.entity.Purchase;
import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.model.Sales;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 금 구매, 판매 응답용 DTO
 */
@Getter
public class InvoiceResponse {
    @Schema(description = "주문ID", example = "1")
    private Long id;

    @Schema(description = "주문 번호", example = "ORD-20240906100629-1000")
    private String orderNumber;

    @Schema(description = "사용자 ID", example = "2")
    private Long userId;

    @Schema(description = "주문 상태", example = "주문 완료")
    private String status;

    @Schema(description = "품목 (99.9 / 99.99)", example = "99.99")
    private Item item;

    @Schema(description = "수량", example = "3.75")
    private BigDecimal quantity; // 수량, 소수점 두 자리까지

    @Schema(description = "금액", example = "406875")
    private int price; // 금액

    @Schema(description = "주문 일자", example = "2024-09-06T10:38:00")
    private LocalDateTime orderDate;

    @QueryProjection
    public InvoiceResponse(Purchase purchase) {
        this.id = purchase.getId();
        this.orderNumber = purchase.getPurchaseNumber();
        this.userId = purchase.getUserId();
        this.status = purchase.getStatus();
        this.item = purchase.getItem();
        this.quantity = purchase.getQuantity();
        this.price = purchase.getPrice();
        this.orderDate = purchase.getOrderDate();
    }

    @QueryProjection
    public InvoiceResponse(Sales sales) {
        this.id = sales.getId();
        this.orderNumber = sales.getSalesNumber();
        this.userId = sales.getUserId();
        this.status = sales.getStatus();
        this.item = sales.getItem();
        this.quantity = sales.getQuantity();
        this.price = sales.getPrice();
        this.orderDate = sales.getOrderDate();
    }
}
