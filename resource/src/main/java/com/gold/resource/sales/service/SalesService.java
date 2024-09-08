package com.gold.resource.sales.service;

import com.gold.resource.common.dto.InvoiceRequest;
import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.invoice.dto.PagingInvoice;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesService {
    InvoiceResponse save(InvoiceRequest salesRequest, Long userId);

    InvoiceResponse getDetail(Long salesId, Long userId);

    PagingInvoice getAll(Long userId, Pageable pageable);

    InvoiceResponse updateSalesStatus(Long salesId, SalesStatusUpdate status, Long userId);
}
