package com.gold.resource.purchase.dto;

import com.gold.resource.common.validation.IsValidSalesState;
import com.gold.resource.purchase.enums.PurchaseStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PurchaseStatusUpdate {
    /**
     * {@link PurchaseStatus}
     */
    @NotBlank(message = "상태는 필수입니다.")
    @IsValidSalesState
    private String purchaseState;

    public PurchaseStatus getStatus() {
        return PurchaseStatus.valueOf(purchaseState);
    }
}