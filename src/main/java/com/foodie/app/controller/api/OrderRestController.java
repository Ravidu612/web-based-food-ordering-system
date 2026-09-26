package com.foodie.app.controller.api;

import com.foodie.app.dto.ApiResponse;
import com.foodie.app.dto.CancelOrderDto;
import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponseDto>> checkout(@AuthenticationPrincipal UserDetails userDetails,
                                                                  @Valid @RequestBody CheckoutDto checkoutDto) {
        Customer customer = getCustomer(userDetails);
        OrderResponseDto order = orderService.processOrderCheckout(customer.getId(), checkoutDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Order placed and payment processed successfully", order));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = getCustomer(userDetails);
        return ResponseEntity.ok(ApiResponse.ok(orderService.getCustomerOrders(customer.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getOrderResponseDtoById(id)));
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @Valid @RequestBody CancelOrderDto dto) {
        Customer customer = getCustomer(userDetails);
        OrderResponseDto cancelledOrder = orderService.cancelOrder(dto.getOrderId(), customer.getId(), dto.getReason());
        return ResponseEntity.ok(ApiResponse.ok("Order cancelled and stock restored successfully", cancelledOrder));
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
    }
}
