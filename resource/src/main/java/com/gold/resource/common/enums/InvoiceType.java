package com.gold.resource.common.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum InvoiceType {
    INVOICE("invoice"),
    PURCHASE("purchase"),
    SALES("sales");

    private String domain;

    public String get() {
        return domain;
    }
}
