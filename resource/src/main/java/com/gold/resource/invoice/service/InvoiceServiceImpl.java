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
        // InvoiceType에 맞는 데이터 호출
        PageImpl<InvoiceResponse> invoices = InvoiceType.PURCHASE.equals(queryParam.getInvoiceType())
                ? invoiceRepository.findPurchaseByUserIdAndFilter(queryParam)
                : invoiceRepository.findSalesByUserIdAndFilter(queryParam);

        // 페이지네이션 링크 생성
        PagingLink pagingLink = createPagingLink(queryParam, invoices.getTotalPages());

        // PagingInvoice 생성 및 반환
        return new PagingInvoice(invoices.getContent(), pagingLink);
    }

    /**
     * 페이지네이션 링크 생성
     *
     * @param queryParam 링크 생성에 필요한 필터 정보와 페이지네이션 정보를 담은 {@link InvoiceQueryParam} 객체
     * @param totalPages 총 페이지 수
     * @return 현재 페이지, 첫 페이지, 마지막 페이지, 이전 페이지, 다음 페이지 링크를 담은 {@link PagingLink} 객체
     */
    private PagingLink createPagingLink(InvoiceQueryParam queryParam, int totalPages) {
        // 현재 페이지
        int currentPage = queryParam.getPageable().getPageNumber() + 1;

        // 페이지 사이즈
        int pageSize = queryParam.getPageable().getPageSize();

        // 구매/판매
        String invoiceType = queryParam.getInvoiceType().get();

        // 어느 엔드포인트에서 호출했는지
        String domain = queryParam.getDomain().get();

        return PagingLink.builder()
                .self(createLink(invoiceType, domain, currentPage, pageSize))
                .first(createLink(invoiceType, domain, 1, pageSize))
                .last(createLink(invoiceType, domain, totalPages, pageSize))
                .prev(currentPage > 1 ? createLink(invoiceType, domain, currentPage - 1, pageSize) : null)
                .next(currentPage < totalPages ? createLink(invoiceType, domain, currentPage + 1, pageSize) : null)
                .build();
    }

    /**
     * 페이지네이션 링크 URL 생성
     *
     * @param invoiceType 구매/판매 구분
     * @param domain 호출된 도메인 (엔드포인트)
     * @param page 현재 페이지 번호
     * @param size 한 페이지에 표시할 항목 수
     * @return 해당 조건에 맞는 링크 URL 문자열
     */
    private String createLink(String invoiceType, String domain, int page, int size) {
        if(invoiceType.equals(domain)) {
            return String.format("%s%s?page=%d&size=%d", BASE_URL, domain, page, size);
        }

        return String.format("%s%s?page=%d&size=%d&invoiceType=%s", BASE_URL, domain, page, size, invoiceType);
    }
}
