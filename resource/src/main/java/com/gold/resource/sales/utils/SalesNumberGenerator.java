package com.gold.resource.sales.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class SalesNumberGenerator {

    private static final AtomicInteger counter = new AtomicInteger(1000);

    public static String generateSalesNumber() {
        // 현재 시각 기반 문자열 생성
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // 1000 ~ 9999 사이의 고유 번호 생성
        int sequentialNumber = counter.getAndIncrement();

        // 만약 9999를 넘으면 다시 1000으로 초기화
        if (sequentialNumber > 9999) {
            counter.set(1000);
        }

        // 주문번호 생성 (ex. "ORD-20240906100629-1000")
        return "ORD-" + dateTime + "-" + sequentialNumber;
    }
}
