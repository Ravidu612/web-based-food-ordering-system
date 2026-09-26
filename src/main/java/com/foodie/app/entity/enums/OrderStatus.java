package com.foodie.app.entity.enums;

public enum OrderStatus {
    PENDING("Order Placed, awaiting confirmation"),
    CONFIRMED("Confirmed by restaurant"),
    PREPARING("Kitchen is preparing your food"),
    OUT_FOR_DELIVERY("Out for delivery with courier"),
    DELIVERED("Delivered successfully"),
    CANCELLED("Order cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
}
