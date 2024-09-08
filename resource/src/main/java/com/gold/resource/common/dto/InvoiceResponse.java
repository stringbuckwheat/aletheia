package com.gold.resource.common.dto;

import com.gold.resource.purchase.entity.Purchase;
import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.model.Sales;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class InvoiceResponse {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String status;
    private Item item; // 품목
    private BigDecimal quantity; // 수량, 소수점 두 자리까지
    private int price; // 금액
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
