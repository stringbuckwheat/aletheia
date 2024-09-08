package com.gold.resource.purchase.service;

import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.purchase.dto.PurchaseStatusUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PurchaseService {
    @Transactional
    InvoiceResponse save(InvoiceRequest purchaseRequest, Long userId);

    @Transactional(readOnly = true)
    InvoiceResponse getDetail(Long purchaseId, Long userId);

    @Transactional(readOnly = true)
    PagingInvoice getAll(Long userId, Pageable pageable);

    // Update -> 본인 것만 수정 가능
    @Transactional
    InvoiceResponse updateState(Long purchaseId, Long userId, PurchaseStatusUpdate status);
}
