package com.gold.resource.purchase.service;

import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.invoice.service.InvoiceService;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.common.error.ErrorMessage;
import com.gold.resource.purchase.dto.PurchaseStatusUpdate;
import com.gold.resource.purchase.entity.Purchase;
import com.gold.resource.purchase.repository.PurchaseRepository;
import com.gold.resource.purchase.utils.PurchaseNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public InvoiceResponse save(InvoiceRequest purchaseRequest, Long userId) {
        String purchaseNumber = PurchaseNumberGenerator.generatePurchaseNumber();
        Purchase purchase = purchaseRepository.save(purchaseRequest.toPurchase(userId, purchaseNumber));
        return new InvoiceResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getDetail(Long purchaseId, Long userId) {
        Purchase purchase = getPurchase(purchaseId, userId);
        return new InvoiceResponse(purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public PagingInvoice getAll(Long userId, Pageable pageable) {
        InvoiceQueryParam queryParam = InvoiceQueryParam.builder()
                .userId(userId)
                .invoiceType(InvoiceType.PURCHASE)
                .pageable(pageable)
                .domain(InvoiceType.PURCHASE)
                .build();

        return invoiceService.getAll(queryParam);
    }

    // Update -> 본인 것만 수정 가능
    @Override
    @Transactional
    public InvoiceResponse updateState(Long purchaseId, Long userId, PurchaseStatusUpdate status) {
        Purchase purchase = getPurchase(purchaseId, userId);
        purchase.updateStatus(status.getStatus());
        return new InvoiceResponse(purchase);
    }

    private Purchase getPurchase(Long purchaseId, Long userId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PURCHASE_NOT_FOUND.getMessage()));

        if(!purchase.getUserId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessage.NOT_YOUR_PURCHASE_INVOICE.getMessage());
        }

        return purchase;
    }
}
