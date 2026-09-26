package com.foodie.app.exception;

public class DuplicateResourceException extends FoodOrderingException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
