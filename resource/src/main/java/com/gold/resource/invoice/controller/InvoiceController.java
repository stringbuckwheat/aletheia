package com.gold.resource.invoice.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.common.doc.ApiDocs;
import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.common.error.ErrorResponse;
import com.gold.resource.invoice.service.InvoiceService;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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

    @ApiDocs(
            summary = "판매/구매 주문 내역 조회",
            description = "본인의 정보만 조회 가능"
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK: 판매/구매 주문 내역",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PagingInvoice.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "BAD_REQUEST: 필수 요청 매개변수 누락되었거나 유효하지 않은 경우",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(
                            value = "{\"message\": \"invoiceType은/는 필수 입력값입니다.\"}"
                    )
            )
    )
    @GetMapping("/api/resource/invoices")
    public ResponseEntity<PagingInvoice> getInvoices(
            @RequestParam(value = "startDate") LocalDate startDate,
            @RequestParam(value = "endDate") LocalDate endDate,
            @RequestParam(value = "offset", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "invoiceType") String invoiceType,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        InvoiceQueryParam queryParam = InvoiceQueryParam.builder()
                .userId(userDetails.getId())
                .startDate(startDate)
                .endDate(endDate)
                .invoiceType(InvoiceType.valueOf(invoiceType))
                .pageable(PageRequest.of(page - 1, limit))
                .domain(InvoiceType.INVOICE)
                .build();

        return ResponseEntity.ok().body(invoiceService.getAll(queryParam));
    }
}
