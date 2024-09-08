package com.gold.resource.invoice.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.invoice.service.InvoiceService;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/api/resource/user/invoices")
    public ResponseEntity<PagingInvoice> getInvoices(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate,
            @RequestParam(value = "offset") int page,
            @RequestParam(value = "limit") int limit,
            @RequestParam(value = "invoiceType", required = false) String invoiceType,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        InvoiceQueryParam queryParam = InvoiceQueryParam.builder()
                .userId(userDetails.getId())
                .startDate(startDate)
                .endDate(endDate)
                .invoiceType(InvoiceType.valueOf(invoiceType))
                .pageable(PageRequest.of(page, limit))
                .domain(InvoiceType.INVOICE)
                .build();

        return ResponseEntity.ok().body(invoiceService.getAll(queryParam));
    }
}
