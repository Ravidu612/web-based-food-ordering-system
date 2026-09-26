package com.foodie.app.exception;

public class InsufficientStockException extends FoodOrderingException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
