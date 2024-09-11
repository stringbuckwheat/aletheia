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

    // 금 구매 정보 저장
    @Override
    @Transactional
    public InvoiceResponse save(InvoiceRequest purchaseRequest, Long userId) {
        // Human Readable한 구매 번호 저장
        String purchaseNumber = PurchaseNumberGenerator.generatePurchaseNumber();

        // 저장
        Purchase purchase = purchaseRepository.save(purchaseRequest.toPurchase(userId, purchaseNumber));
        return new InvoiceResponse(purchase);
    }

    // 금 구매 상세 내역
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getDetail(Long purchaseId, Long userId) {
        Purchase purchase = getPurchase(purchaseId, userId);
        return new InvoiceResponse(purchase);
    }

    // 해당 사용자의 모든 금 구매 내역
    @Override
    @Transactional(readOnly = true)
    public PagingInvoice getAll(Long userId, InvoiceQueryParam invoiceQueryParam) {
        return invoiceService.getAll(invoiceQueryParam);
    }

    // 금 구매 상태 변경
    @Override
    @Transactional
    public InvoiceResponse updateState(Long purchaseId, Long userId, PurchaseStatusUpdate status) {
        Purchase purchase = getPurchase(purchaseId, userId);
        purchase.updateStatus(status.toStatus()); // 상태 변경
        return new InvoiceResponse(purchase);
    }

    private Purchase getPurchase(Long purchaseId, Long userId) {
        // ID를 기준으로 조회
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.PURCHASE_NOT_FOUND.getMessage()));

        // 소유자 검증
        if(!purchase.getUserId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessage.NOT_YOUR_INVOICE.getMessage());
        }

        return purchase;
    }
}
