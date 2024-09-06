package com.gold.resource.sales.dto;

import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.model.Sales;

import java.math.BigDecimal;

public class SalesOverview {
    private Long id;
    private Long userId;
    private Item item; // 품목
    private BigDecimal quantity; // 수량, 소수점 두 자리까지
    private int price; // 금액

    public SalesOverview(Sales sales) {
        this.id = sales.getId();
        this.userId = sales.getUserId();
        this.item = sales.getItem();
        this.quantity = sales.getQuantity();
        this.price = sales.getPrice();
    }
}
