package com.gold.resource.sales.service;

import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import org.springframework.transaction.annotation.Transactional;

public interface SalesAdminService {
    @Transactional
    InvoiceResponse updateSalesOrderStatus(Long salesId, SalesStatusUpdate status);
}
