package com.gold.resource.common.dto;

import com.gold.resource.common.validation.IsValidItem;
import com.gold.resource.purchase.entity.Purchase;
import com.gold.resource.purchase.enums.PurchaseStatus;
import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.enums.SalesStatus;
import com.gold.resource.sales.model.Sales;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 금 구매.판매 요청용 DTO
 */
@ToString
@Getter
public class InvoiceRequest {
    /**
     * {@link Item}의 값 중 하나
     */
    @Schema(description = "품목 (99.9 / 99.99)", example = "99.99")
    @NotNull(message = "품목은 필수입니다.")
    @IsValidItem
    private String item;

    @Schema(description = "수량", example = "3.75")
    @NotNull(message = "수량은 필수입니다.")
    @DecimalMin(value = "0.01", message = "수량은 최소 0.01g 이상이어야 합니다.")
    @Digits(integer = 10, fraction = 2, message = "수량은 최대 소수점 둘째 자리까지 입력 가능합니다.")
    private BigDecimal quantity;

    @Schema(description = "금액", example = "406875")
    @Min(value = 1, message = "금액은 0보다 커야 합니다.")
    private int price;

    @Schema(description = "주소", example = "서울시 종로구")
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    public Sales toSales(Long userId, String salesNumber) {
        return Sales.builder()
                .salesNumber(salesNumber)
                .userId(userId)
                .status(SalesStatus.ORDER_COMPLETED)
                .item(Item.fromValue(item))
                .quantity(quantity)
                .price(price)
                .address(address)
                .build();
    }

    public Purchase toPurchase(Long userId, String purchaseNumber) {
        return Purchase.builder()
                .purchaseNumber(purchaseNumber)
                .userId(userId)
                .status(PurchaseStatus.ORDER_COMPLETED)
                .item(Item.fromValue(item))
                .quantity(quantity)
                .price(price)
                .address(address)
                .build();
    }
}
