package com.gold.resource.sales.service;

import com.gold.resource.common.error.ErrorMessage;
import com.gold.resource.sales.dto.SalesRequest;
import com.gold.resource.sales.dto.SalesDetail;
import com.gold.resource.sales.dto.SalesOverview;
import com.gold.resource.sales.model.Sales;
import com.gold.resource.sales.repository.SalesRepository;
import com.gold.resource.sales.utils.SalesNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;

    @Override
    @Transactional
    public SalesDetail createSalesOrder(SalesRequest salesRequest, Long userId) {
        String salesNumber = SalesNumberGenerator.generateSalesNumber();
        Sales sales = salesRepository.save(salesRequest.toEntityWith(userId, salesNumber));
        return new SalesDetail(sales);
    }

    @Override
    @Transactional(readOnly = true)
    public SalesDetail getDetail(Long salesId, Long userId) {
        Sales sales = salesRepository.findById(salesId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.SALES_NOT_FOUND.getMessage()));

        if(!sales.getUserId().equals(userId)) {
            throw new AccessDeniedException(ErrorMessage.NOT_YOUR_SALES_INVOICE.getMessage());
        }

        return new SalesDetail(sales);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOverview> getAll(Long userId) {
        // TODO 페이징 필요
        return salesRepository.findByUserId(userId).stream()
                .map(SalesOverview::new)
                .toList();
    }
}
