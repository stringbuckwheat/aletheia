package com.gold.resource.sales.service;

import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

public interface SalesService {
    /**
     * 금 판매 주문 저장
     * @param salesRequest 금 주문 요청 정보
     * @param userId 유저ID
     * @return 저장된 금 주문 정보를 담은 {@link InvoiceResponse} 객체
     */
    @Transactional
    InvoiceResponse save(InvoiceRequest salesRequest, Long userId);

    /**
     * 금 판매 주문 ID(PK)를 기반으로 주문 상세내역 조회
     * 요청된 금 주문 내역이 본인(사용자 ID)의 것일때만 조회
     *
     * @param salesId 금 주문 ID
     * @param userId 사용자 ID
     * @return 금 주문 정보를 담은 {@link InvoiceResponse} 객체
     * @throws NoSuchElementException 금 주문 ID에 해당하는 주문 정보가 없는 경우
     * @throws AccessDeniedException 요청된 금 주문 내역이 본인(사용자 ID)의 것이 아닌 경우
     */
    @Transactional(readOnly = true)
    InvoiceResponse getDetail(Long salesId, Long userId);

    /**
     * 해당 사용자의 모든 금 판매 내역 조회
     * 페이징 적용
     *
     * @param userId 사용자 ID
     * @param invoiceQueryParam 필터링 및 페이지네이션 정보를 담은 {@link InvoiceQueryParam} 객체
     * @return 성공 여부, 메시지, 주문 내역, 링크 등을 포함하는 {@link PagingInvoice} 객체
     */
    @Transactional(readOnly = true)
    PagingInvoice getAll(Long userId, InvoiceQueryParam invoiceQueryParam);

    /**
     * 금 판매 상태 변경
     * 요청된 금 주문 내역이 본인(사용자 ID)의 것일때만 변경 가능
     *
     * @param salesId 금 주문 ID
     * @param status 변경할 금 주문 상태 정보
     * @param userId 사용자 ID
     * @return 변경된 금 주문 정보를 담은 {@link InvoiceResponse} 객체
     * @throws NoSuchElementException 금 주문 ID에 해당하는 주문 정보가 없는 경우
     * @throws AccessDeniedException 요청된 금 주문 내역이 본인(사용자 ID)의 것이 아닌 경우
     */
    @Transactional
    InvoiceResponse updateSalesStatus(Long salesId, SalesStatusUpdate status, Long userId);
}
