package com.gold.resource.sales.dto;

import com.gold.resource.common.validation.IsValidSalesState;
import com.gold.resource.sales.enums.SalesStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SalesStatusUpdate {
    @NotBlank(message = "상태는 필수입니다.")
    @IsValidSalesState
    private String salesStatus;

    public SalesStatus getStatus() {
        return SalesStatus.valueOf(salesStatus);
    }
}
