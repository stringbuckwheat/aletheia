package com.gold.resource.invoice.dto;

import com.gold.resource.common.enums.InvoiceType;
import lombok.*;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@ToString
public class InvoiceQueryParam {
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private InvoiceType invoiceType; // 조회 대상(판매, 구매)
    private Pageable pageable;
    private InvoiceType domain; // 어느 엔드포인트 path로 요청했는지
}
