package com.gold.resource.sales.service;

import com.gold.resource.sales.dto.SalesDetail;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import org.springframework.transaction.annotation.Transactional;

public interface SalesAdminService {
    @Transactional
    SalesDetail updateSalesOrderStatus(Long salesId, SalesStatusUpdate status);
}
