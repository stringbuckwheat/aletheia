package com.gold.resource.purchase.entity;

import com.gold.resource.purchase.enums.PurchaseStatus;
import com.gold.resource.sales.enums.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 금 매입
 */
@Entity
@Table(name = "purchase")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_number", nullable = false, unique = true)
    private String purchaseNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status;

    @Column(name = "item", nullable = false)
    private Item item; // 품목 (99.9, 99.99)

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // 수량, 소수점 두 자리까지

    @Column(name = "price", nullable = false)
    private int price; // 금액

    @Column(name = "address", nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Builder
    public Purchase(Long userId, String purchaseNumber, PurchaseStatus status, Item item, BigDecimal quantity, int price, String address) {
        this.userId = userId;
        this.purchaseNumber = purchaseNumber;
        this.status = status.getDescription();
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.address = address;
        this.orderDate = LocalDateTime.now();
    }

    // 주문 상태 업데이트
    public void updateStatus(PurchaseStatus newStatus) {
        this.status = newStatus.getDescription();
    }
}
