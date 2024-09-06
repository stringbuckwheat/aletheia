package com.gold.resource.sales.enums;

public enum Item {
    GOLD_999, GOLD_9999;

    public static Item fromValue(String value) {
        try {
            return Item.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}