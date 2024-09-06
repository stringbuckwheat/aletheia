package com.gold.resource.sales.service;

import com.gold.resource.sales.dto.SalesDetail;
import com.gold.resource.sales.dto.SalesOverview;
import com.gold.resource.sales.dto.SalesRequest;

import java.util.List;

public interface SalesService {
    SalesDetail createSalesOrder(SalesRequest salesRequest, Long userId);

    SalesDetail getDetail(Long salesId, Long userId);

    List<SalesOverview> getAll(Long userId);
}
