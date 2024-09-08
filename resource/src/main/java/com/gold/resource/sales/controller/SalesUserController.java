package com.gold.resource.sales.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import com.gold.resource.sales.service.SalesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class SalesUserController {

    private final SalesServiceImpl salesService;

    // 판매 주문 생성
    @PostMapping("/api/resource/user/sales")
    public ResponseEntity<InvoiceResponse> save(@RequestBody @Valid InvoiceRequest salesRequest,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salesService.save(salesRequest, userDetails.getId()));
    }

    // 판매 주문 상세 조회
    @GetMapping("/api/resource/user/sales/{salesId}")
    public ResponseEntity<InvoiceResponse> getDetail(@PathVariable(name = "salesId") Long salesId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(salesService.getDetail(salesId, userDetails.getId()));
    }

    // 내 모든 판매 내역 조회
    @GetMapping("/api/resource/user/sales")
    public ResponseEntity<PagingInvoice> getAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam(value = "offset") int page,
                                                @RequestParam(value = "limit") int limit
    ) {
        PagingInvoice salesOverviewList = salesService.getAll(userDetails.getId(), PageRequest.of(page, limit));
        return ResponseEntity.ok(salesOverviewList);
    }

    // 판매 상태 수정
    @PutMapping("/api/resource/user/sales/{salesId}/status")
    public ResponseEntity<InvoiceResponse> updateState(@PathVariable(name = "salesId") Long salesId,
                                                       @RequestBody @Valid SalesStatusUpdate status,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(salesService.updateSalesStatus(salesId, status, userDetails.getId()));
    }
}
