package com.gold.resource.invoice.service;

import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.invoice.repository.InvoiceRepository;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.invoice.dto.PagingLink;
import com.gold.resource.common.dto.InvoiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private static final String BASE_URL = "http://localhost:9999/api/resource/";

    @Override
    @Transactional(readOnly = true)
    public PagingInvoice getAll(InvoiceQueryParam queryParam) {
        PageImpl<InvoiceResponse> invoices = InvoiceType.PURCHASE.equals(queryParam.getInvoiceType())
                ? invoiceRepository.findPurchaseByUserIdAndFilter(queryParam)
                : invoiceRepository.findSalesByUserIdAndFilter(queryParam);

        // 페이지네이션 링크 생성
        PagingLink pagingLink = createPagingLink(queryParam, invoices);

        // PagingInvoice 생성 및 반환
        return new PagingInvoice(invoices.getContent(), pagingLink);
    }

    private PagingLink createPagingLink(InvoiceQueryParam queryParam, PageImpl<InvoiceResponse> invoices) {
        int currentPage = queryParam.getPageable().getPageNumber() + 1;
        int pageSize = queryParam.getPageable().getPageSize();
        int totalPages = invoices.getTotalPages();
        String invoiceType = queryParam.getInvoiceType().get();
        String domain = queryParam.getDomain().get();

        return PagingLink.builder()
                .self(createLink(invoiceType, domain, currentPage, pageSize))
                .first(createLink(invoiceType, domain, 1, pageSize))
                .last(createLink(invoiceType, domain, totalPages, pageSize))
                .prev(currentPage > 1 ? createLink(invoiceType, domain, currentPage - 1, pageSize) : null)
                .next(currentPage < totalPages ? createLink(invoiceType, domain, currentPage + 1, pageSize) : null)
                .build();
    }

    private String createLink(String invoiceType, String domain, int page, int size) {
        return String.format("%s%s?invoiceType=%s&page=%d&size=%d", BASE_URL, domain, invoiceType, page, size);
    }
}
