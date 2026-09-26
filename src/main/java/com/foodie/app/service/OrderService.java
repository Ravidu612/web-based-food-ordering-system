package com.foodie.app.service;

import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.Order;
import com.foodie.app.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDto processOrderCheckout(Long customerId, CheckoutDto checkoutDto);
    List<OrderResponseDto> getCustomerOrders(Long customerId);
    OrderResponseDto getOrderResponseDtoById(Long orderId);
    Order getOrderById(Long orderId);
    OrderResponseDto cancelOrder(Long orderId, Long customerId, String reason);
    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus, String cancellationReason);
    List<OrderResponseDto> getAllOrdersAdmin();
    long countActiveOrders();
}
