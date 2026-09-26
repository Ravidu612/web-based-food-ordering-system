package com.foodie.app.service;

import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.entity.Order;
import com.foodie.app.entity.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    Payment processPayment(Order order, BigDecimal amount, CheckoutDto checkoutDto);
    List<Payment> getAllPayments();
    Payment getPaymentByOrderId(Long orderId);
}
