package com.gold.resource.invoice.repository;

import com.gold.resource.common.dto.InvoiceResponse;
import com.gold.resource.common.dto.QInvoiceResponse;
import com.gold.resource.invoice.dto.InvoiceQueryParam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.gold.resource.purchase.entity.QPurchase.purchase;
import static com.gold.resource.sales.model.QSales.sales;

@RequiredArgsConstructor
@Repository
@Slf4j
public class InvoiceRepository {
    private final JPAQueryFactory queryFactory;

    public PageImpl<InvoiceResponse> findPurchaseByUserIdAndFilter(InvoiceQueryParam queryParam) {
        Long userId = queryParam.getUserId();

        LocalDateTime startOfDay = queryParam.getStartDate() != null ? queryParam.getStartDate().atStartOfDay() : null;
        LocalDateTime endOfDay = queryParam.getEndDate() != null ? queryParam.getEndDate().atTime(23, 59, 59, 999999) : null;

        Pageable pageable = queryParam.getPageable();

        boolean isDateValid = startOfDay != null && endOfDay != null;

        // 동적 쿼리 작성
        List<InvoiceResponse> results = queryFactory
                .select(new QInvoiceResponse(purchase))
                .from(purchase)
                .where(
                        purchase.userId.eq(userId),
                        isDateValid ? purchase.orderDate.between(startOfDay, endOfDay) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(purchase.orderDate.desc())
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory.select(purchase.count())
                .from(purchase)
                .where(
                        purchase.userId.eq(userId),
                        isDateValid ? purchase.orderDate.between(startOfDay, endOfDay) : null
                )
                .fetchOne();

        total = total == null ? 0 : total;

        return new PageImpl<InvoiceResponse>(results, pageable, total);
    }

    public PageImpl<InvoiceResponse> findSalesByUserIdAndFilter(InvoiceQueryParam queryParam) {
        Long userId = queryParam.getUserId();

        LocalDateTime startOfDay = queryParam.getStartDate() != null ? queryParam.getStartDate().atStartOfDay() : null;
        LocalDateTime endOfDay = queryParam.getEndDate() != null ? queryParam.getEndDate().atTime(23, 59, 59, 999999) : null;

        Pageable pageable = queryParam.getPageable();

        boolean isDateValid = startOfDay != null && endOfDay != null;

        // 동적 쿼리 작성
        List<InvoiceResponse> results = queryFactory
                .select(new QInvoiceResponse(sales))
                .from(sales)
                .where(
                        sales.userId.eq(userId),
                        isDateValid ? sales.orderDate.between(startOfDay, endOfDay) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sales.orderDate.desc())
                .fetch();

        // 전체 개수 조회
        Long total = queryFactory.select(sales.count())
                .from(sales)
                .where(
                        sales.userId.eq(userId),
                        isDateValid ? sales.orderDate.between(startOfDay, endOfDay) : null
                )
                .fetchOne();

        total = total == null ? 0 : total;

        return new PageImpl<InvoiceResponse>(results, pageable, total);
    }
}
