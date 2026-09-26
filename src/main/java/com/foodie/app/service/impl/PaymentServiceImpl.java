package com.foodie.app.service.impl;

import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.entity.Order;
import com.foodie.app.entity.Payment;
import com.foodie.app.entity.enums.PaymentStatus;
import com.foodie.app.exception.PaymentFailedException;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.PaymentRepository;
import com.foodie.app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment processPayment(Order order, BigDecimal amount, CheckoutDto checkoutDto) {
        // Validate card attributes format
        if (checkoutDto.getCardNumber() == null || checkoutDto.getCardNumber().length() != 16) {
            throw new PaymentFailedException("Invalid card number. 16 digits required.");
        }

        // Generate mock transaction reference
        String transactionId = "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        String lastFour = checkoutDto.getCardNumber().substring(checkoutDto.getCardNumber().length() - 4);

        Payment payment = Payment.builder()
                .order(order)
                .transactionId(transactionId)
                .paymentMethod("CARD")
                .amount(amount)
                .paymentStatus(PaymentStatus.SUCCESS)
                .cardLastFour(lastFour)
                .responseCode("200_OK")
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for order: " + orderId));
    }
}
