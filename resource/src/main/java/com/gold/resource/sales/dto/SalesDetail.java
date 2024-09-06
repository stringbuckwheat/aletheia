package com.gold.resource.sales.dto;

import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.model.Sales;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SalesDetail {
    private Long id;
    private String salesNumber; // 판매 주문 번호
    private String status;
    private Item item;
    private BigDecimal quantity;
    private int price;
    private  String address;

    public SalesDetail(Sales sales) {
        this.id = sales.getId();
        this.salesNumber = sales.getSalesNumber();
        this.status = sales.getStatus();
        this.item = sales.getItem();
        this.quantity = sales.getQuantity();
        this.price = sales.getPrice();
        this.address = sales.getAddress();
    }
}
