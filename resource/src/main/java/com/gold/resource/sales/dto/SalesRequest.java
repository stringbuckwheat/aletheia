package com.gold.resource.sales.dto;

import com.gold.resource.common.validation.IsValidItem;
import com.gold.resource.sales.enums.SalesStatus;
import com.gold.resource.sales.enums.Item;
import com.gold.resource.sales.model.Sales;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
public class SalesRequest {
    @NotNull(message = "품목은 필수입니다.")
    @IsValidItem
    private String item;

    @NotNull(message = "수량은 필수입니다.")
    @DecimalMin(value = "0.01", message = "수량은 최소 0.01g 이상이어야 합니다.")
    @Digits(integer = 10, fraction = 2, message = "수량은 최대 소수점 둘째 자리까지 입력 가능합니다.")
    private BigDecimal quantity;

    @Min(value = 1, message = "금액은 0보다 커야 합니다.")
    private int price;

    @NotBlank(message = "판매자 주소는 필수입니다.")
    private String address;

    public Sales toEntityWith(Long userId, String salesNumber) {
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
}
