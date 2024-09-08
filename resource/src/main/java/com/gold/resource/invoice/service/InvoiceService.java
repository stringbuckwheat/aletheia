package com.gold.resource.invoice.service;

import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.gold.resource.invoice.dto.PagingInvoice;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceService {
    @Transactional(readOnly = true)
    PagingInvoice getAll(InvoiceQueryParam queryParam);
}
