package com.gold.resource.sales.controller;

import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.sales.service.SalesAdminServiceImpl;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SalesAdminController {
    private final SalesAdminServiceImpl salesService;

    // 판매 주문 상태 업데이트 (관리자만 가능)
    @PutMapping("/api/resource/admin/sales/{salesId}/status")
    public ResponseEntity<InvoiceResponse> updateSalesOrderStatus(
            @PathVariable(name = "salesId") Long salesId,
            @RequestBody @Valid SalesStatusUpdate status) {
        return ResponseEntity.ok().body(salesService.updateSalesOrderStatus(salesId, status));
    }
}
