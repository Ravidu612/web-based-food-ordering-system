package com.foodie.app.exception;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorCode", "404 - Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/custom-error";
    }

    @ExceptionHandler(InsufficientStockException.class)
    public String handleStockError(InsufficientStockException ex, Model model) {
        model.addAttribute("errorCode", "Stock Unavailable");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/custom-error";
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public String handleInvalidState(InvalidOrderStateException ex, Model model) {
        model.addAttribute("errorCode", "Invalid Order Action");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/custom-error";
    }

    @ExceptionHandler(PaymentFailedException.class)
    public String handlePaymentError(PaymentFailedException ex, Model model) {
        model.addAttribute("errorCode", "Payment Processing Failed");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/custom-error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("errorCode", "500 - Server Error");
        model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
        return "error/custom-error";
    }
}
