package com.foodie.app.dto;

import com.foodie.app.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private OrderStatus orderStatus;
    private String statusDescription;
    private BigDecimal subtotalAmount;
    private BigDecimal taxAmount;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private String contactPhone;
    private String deliveryNotes;
    private String cancellationReason;
    private Boolean canBeCancelled;
    private LocalDateTime orderDate;

    // Payment details
    private String transactionId;
    private String paymentMethod;
    private String paymentStatus;
    private String cardLastFour;

    @Builder.Default
    private List<OrderItemDto> items = new ArrayList<>();
}
