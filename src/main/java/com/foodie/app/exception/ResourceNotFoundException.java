package com.foodie.app.exception;

public class ResourceNotFoundException extends FoodOrderingException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
