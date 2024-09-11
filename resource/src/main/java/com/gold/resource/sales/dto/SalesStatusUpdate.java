package com.gold.resource.sales.dto;

import com.gold.resource.common.validation.IsValidSalesState;
import com.gold.resource.sales.enums.SalesStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SalesStatusUpdate {
    @NotBlank(message = "판매 상태는 필수입니다.")
    @IsValidSalesState
    @Schema(description = "판매 상태", example = "PAYMENT_COMPLETED")
    private String salesStatus;

    public SalesStatus toStatus() {
        return SalesStatus.valueOf(salesStatus);
    }
}
