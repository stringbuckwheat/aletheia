package com.gold.resource.sales.service;

import com.gold.resource.common.error.ErrorMessage;
import com.gold.resource.sales.dto.SalesDetail;
import com.gold.resource.sales.dto.SalesStatusUpdate;
import com.gold.resource.sales.model.Sales;
import com.gold.resource.sales.repository.SalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesAdminServiceImpl implements SalesAdminService {
    private final SalesRepository salesRepository;

    @Override
    @Transactional
    public SalesDetail updateSalesOrderStatus(Long salesId, SalesStatusUpdate status) {
        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.SALES_NOT_FOUND.getMessage()));

        sales.updateStatus(status.getStatus());  // 상태 변경

        return new SalesDetail(sales);
    }
}
