package com.foodie.app.exception;

public class PaymentFailedException extends FoodOrderingException {
    public PaymentFailedException(String message) {
        super(message);
    }
}
