package com.foodie.app.exception;

public class FoodOrderingException extends RuntimeException {
    public FoodOrderingException(String message) {
        super(message);
    }

    public FoodOrderingException(String message, Throwable cause) {
        super(message, cause);
    }
}
