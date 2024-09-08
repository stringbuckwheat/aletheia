package com.gold.resource.sales.model;

import com.gold.resource.sales.enums.SalesStatus;
import com.gold.resource.sales.enums.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sales_number", nullable = false, unique = true)
    private String salesNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "item", nullable = false)
    private Item item; // 품목

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // 수량, 소수점 두 자리까지

    @Column(name = "price", nullable = false)
    private int price; // 금액

    @Column(name = "address", nullable = false)
    private String address; // 판매자 주소

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Builder
    public Sales(String salesNumber, Long userId, SalesStatus status, Item item, BigDecimal quantity, int price, String address) {
        this.salesNumber = salesNumber;
        this.userId = userId;
        this.status = status.getDescription();
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.address = address;
        this.orderDate = LocalDateTime.now();
    }

    // 주문 상태 업데이트
    public void updateStatus(SalesStatus newStatus) {
        this.status = newStatus.getDescription();
    }
}
