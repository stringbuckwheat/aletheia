package com.gold.resource.purchase.dto;

import com.gold.resource.common.validation.IsValidSalesState;
import com.gold.resource.purchase.enums.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PurchaseStatusUpdate {
    /**
     * {@link PurchaseStatus}
     */
    @Schema(description = "금 구매 상태", example = "PAYMENT_COMPLETED")
    @NotBlank(message = "상태는 필수입니다.")
    @IsValidSalesState
    private String purchaseState;

    public PurchaseStatus toStatus() {
        return PurchaseStatus.valueOf(purchaseState);
    }
}