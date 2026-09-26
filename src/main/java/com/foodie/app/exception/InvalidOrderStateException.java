package com.foodie.app.exception;

public class InvalidOrderStateException extends FoodOrderingException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
