package com.foodie.app.exception;

public class UnauthorizedAccessException extends FoodOrderingException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
