package com.gold.resource.purchase.controller;

import com.gold.resource.auth.dto.CustomUserDetails;
import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.purchase.dto.PurchaseStatusUpdate;
import com.gold.resource.purchase.service.PurchaseService;
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
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping("/api/resource/user/purchase")
    public ResponseEntity<InvoiceResponse> save(@RequestBody @Valid InvoiceRequest purchaseRequest,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.save(purchaseRequest, userDetails.getId()));
    }

    @GetMapping("/api/resource/user/purchase")
    public ResponseEntity<PagingInvoice> getAll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                @RequestParam(value = "offset", defaultValue = "1") int page,
                                                @RequestParam(value = "limit", defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok().body(purchaseService.getAll(userDetails.getId(), PageRequest.of(page - 1, limit)));
    }

    @GetMapping("/api/resource/user/purchase/{purchaseId}")
    public ResponseEntity<InvoiceResponse> getDetail(
            @PathVariable(name = "purchaseId") Long purchaseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(purchaseService.getDetail(purchaseId, userDetails.getId()));
    }

    @PutMapping("/api/resource/user/purchase/{purchaseId}/status")
    public ResponseEntity<InvoiceResponse> updateState(
            @PathVariable(name = "purchaseId") Long purchaseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid PurchaseStatusUpdate statusUpdate) {
        return ResponseEntity.ok().body(purchaseService.updateState(purchaseId, userDetails.getId(), statusUpdate));
    }
}
