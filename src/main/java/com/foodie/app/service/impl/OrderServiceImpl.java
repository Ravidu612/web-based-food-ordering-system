package com.foodie.app.service.impl;

import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.dto.OrderItemDto;
import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.*;
import com.foodie.app.entity.enums.OrderStatus;
import com.foodie.app.exception.FoodOrderingException;
import com.foodie.app.exception.InvalidOrderStateException;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.exception.UnauthorizedAccessException;
import com.foodie.app.repository.*;
import com.foodie.app.service.CartService;
import com.foodie.app.service.InventoryService;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final SaleRepository saleRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final CartService cartService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto processOrderCheckout(Long customerId, CheckoutDto dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Active shopping cart not found for customer: " + customerId));

        if (cart.getCartItems().isEmpty()) {
            throw new FoodOrderingException("Your shopping cart is empty. Please add items before checking out.");
        }

        // 1. Verify Inventory Stock
        for (CartItem item : cart.getCartItems()) {
            inventoryService.verifyStockAvailability(item.getFood().getId(), item.getQuantity());
        }

        // 2. Compute Totals
        BigDecimal subtotal = cart.getTotalAmount();
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal deliveryFee = BigDecimal.valueOf(3.50);
        BigDecimal total = subtotal.add(tax).add(deliveryFee).setScale(2, RoundingMode.HALF_UP);

        // 3. Generate Order Number
        String orderNumber = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + (1000 + new Random().nextInt(9000));

        Order order = Order.builder()
                .orderNumber(orderNumber)
                .customer(customer)
                .orderStatus(OrderStatus.CONFIRMED)
                .subtotalAmount(subtotal)
                .taxAmount(tax)
                .deliveryFee(deliveryFee)
                .totalAmount(total)
                .deliveryAddress(dto.getDeliveryAddress())
                .contactPhone(dto.getContactPhone())
                .deliveryNotes(dto.getDeliveryNotes())
                .build();

        Order savedOrder = orderRepository.save(order);

        // 4. Create Order Item Snapshots and Decrement Inventory
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalCogs = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .food(cartItem.getFood())
                    .foodName(cartItem.getFood().getName())
                    .unitPrice(cartItem.getUnitPrice())
                    .quantity(cartItem.getQuantity())
                    .subtotal(cartItem.getSubtotal())
                    .build();
            orderItems.add(orderItem);

            BigDecimal itemCost = (cartItem.getFood().getCostPrice() != null)
                    ? cartItem.getFood().getCostPrice() : BigDecimal.ZERO;
            totalCogs = totalCogs.add(itemCost.multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            // Decrement Stock
            inventoryService.decrementStock(cartItem.getFood().getId(), cartItem.getQuantity());
        }

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        // 5. Process Simulated Payment
        Payment payment = paymentService.processPayment(savedOrder, total, dto);
        savedOrder.setPayment(payment);

        // 6. Create Initial Sale Ledger Record
        Sale sale = Sale.builder()
                .order(savedOrder)
                .totalRevenue(total)
                .totalCogs(totalCogs)
                .netMargin(total.subtract(totalCogs))
                .saleDate(LocalDate.now())
                .build();
        saleRepository.save(sale);
        savedOrder.setSale(sale);

        // 7. Clear Shopping Cart
        cartService.clearCart(customerId);

        return mapToDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getCustomerOrders(Long customerId) {
        return orderRepository.findByCustomerIdOrderByOrderDateDesc(customerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderResponseDtoById(Long orderId) {
        return mapToDto(getOrderById(orderId));
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto cancelOrder(Long orderId, Long customerId, String reason) {
        Order order = getOrderById(orderId);

        if (customerId != null && !order.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedAccessException("You are not authorized to cancel this order.");
        }

        if (!order.canBeCancelled()) {
            throw new InvalidOrderStateException("Order cannot be cancelled in state: " + order.getOrderStatus().getDescription());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason != null ? reason : "Cancelled by customer");
        orderRepository.save(order);

        // Restore stock
        inventoryService.restoreStock(order.getOrderItems());

        return mapToDto(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus, String cancellationReason) {
        Order order = getOrderById(orderId);

        if (newStatus == OrderStatus.CANCELLED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
            order.setCancellationReason(cancellationReason != null ? cancellationReason : "Cancelled by admin");
            inventoryService.restoreStock(order.getOrderItems());
        } else {
            order.setOrderStatus(newStatus);
        }

        Order updated = orderRepository.save(order);
        return mapToDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrdersAdmin() {
        return orderRepository.findAll().stream()
                .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public long countActiveOrders() {
        return orderRepository.countActiveOrders();
    }

    private OrderResponseDto mapToDto(Order order) {
        String txnId = order.getPayment() != null ? order.getPayment().getTransactionId() : "N/A";
        String payMethod = order.getPayment() != null ? order.getPayment().getPaymentMethod() : "CARD";
        String payStatus = order.getPayment() != null ? order.getPayment().getPaymentStatus().name() : "SUCCESS";
        String lastFour = order.getPayment() != null ? order.getPayment().getCardLastFour() : "****";

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .customerId(order.getCustomer().getId())
                .customerName(order.getCustomer().getFullName())
                .customerEmail(order.getCustomer().getUser().getEmail())
                .orderStatus(order.getOrderStatus())
                .statusDescription(order.getOrderStatus().getDescription())
                .subtotalAmount(order.getSubtotalAmount())
                .taxAmount(order.getTaxAmount())
                .deliveryFee(order.getDeliveryFee())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .contactPhone(order.getContactPhone())
                .deliveryNotes(order.getDeliveryNotes())
                .cancellationReason(order.getCancellationReason())
                .canBeCancelled(order.canBeCancelled())
                .orderDate(order.getOrderDate())
                .transactionId(txnId)
                .paymentMethod(payMethod)
                .paymentStatus(payStatus)
                .cardLastFour(lastFour)
                .items(order.getOrderItems().stream().map(item -> OrderItemDto.builder()
                        .id(item.getId())
                        .foodId(item.getFood() != null ? item.getFood().getId() : null)
                        .foodName(item.getFoodName())
                        .foodImageUrl(item.getFood() != null ? item.getFood().getImageUrl() : null)
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getSubtotal())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }
}
