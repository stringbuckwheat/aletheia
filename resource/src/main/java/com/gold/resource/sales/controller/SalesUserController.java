package com.gold.resource.sales.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.sales.dto.SalesDetail;
import com.gold.resource.sales.dto.SalesOverview;
import com.gold.resource.sales.dto.SalesRequest;
import com.gold.resource.sales.service.SalesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SalesUserController {

    private final SalesServiceImpl salesService;

    // 판매 주문 생성
    @PostMapping("/api/resource/user/sales")
    public ResponseEntity<SalesDetail> createSalesOrder(@RequestBody @Valid SalesRequest salesRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        SalesDetail salesDetail = salesService.createSalesOrder(salesRequest, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(salesDetail);
    }

    // 판매 주문 상세 조회
    @GetMapping("/api/resource/user/sales/{salesId}")
    public ResponseEntity<SalesDetail> getDetail(@PathVariable(name = "salesId") Long salesId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        SalesDetail salesDetail = salesService.getDetail(salesId, userDetails.getId());
        return ResponseEntity.ok(salesDetail);
    }

    // 내 모든 판매 내역 조회
    @GetMapping("/api/resource/user/sales")
    public ResponseEntity<List<SalesOverview>> getAllSalesOrders(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // TODO 페이징
        List<SalesOverview> salesOverviewList = salesService.getAll(userDetails.getId());
        return ResponseEntity.ok(salesOverviewList);
    }
}
