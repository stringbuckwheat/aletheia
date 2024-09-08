package com.gold.resource.sales.service;

import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.common.enums.InvoiceType;
import com.gold.resource.common.error.ErrorMessage;
import com.gold.resource.invoice.service.InvoiceService;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import com.gold.resource.sales.model.Sales;
import com.gold.resource.sales.repository.SalesRepository;
import com.gold.resource.sales.utils.SalesNumberGenerator;
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
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public InvoiceResponse save(InvoiceRequest salesRequest, Long userId) {
        String salesNumber = SalesNumberGenerator.generateSalesNumber();
        Sales sales = salesRepository.save(salesRequest.toSales(userId, salesNumber));
        return new InvoiceResponse(sales);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getDetail(Long salesId, Long userId) {
        Sales sales = getSales(salesId, userId);
        return new InvoiceResponse(sales);
    }

    @Override
    @Transactional(readOnly = true)
    public PagingInvoice getAll(Long userId, Pageable pageable) {
        InvoiceQueryParam queryParam = InvoiceQueryParam.builder()
                .userId(userId)
                .invoiceType(InvoiceType.SALES)
                .pageable(pageable)
                .domain(InvoiceType.SALES)
                .build();

        return invoiceService.getAll(queryParam);
    }

    @Override
    @Transactional
    public InvoiceResponse updateSalesStatus(Long salesId, SalesStatusUpdate status, Long userId) {
        Sales sales = getSales(salesId, userId);

        sales.updateStatus(status.getStatus());  // 상태 변경

        return new InvoiceResponse(sales);
    }

    private Sales getSales(Long salesId, Long userId) {
        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.SALES_NOT_FOUND.getMessage()));

        if(!sales.getUserId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessage.NOT_YOUR_SALES_INVOICE.getMessage());
        }

        return sales;
    }
}