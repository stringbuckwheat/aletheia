package com.gold.resource.invoice.service;

import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceService {
    /**
     * 판매/구매 주문 내역 조회
     *
     * @param queryParam 필터링 및 페이지네이션 정보를 담은 {@link InvoiceQueryParam} 객체
     * @return 판매 또는 구매 내역을 담은 {@link PagingInvoice} 객체
     */
    @Transactional(readOnly = true)
    PagingInvoice getAll(InvoiceQueryParam queryParam);
}
