package com.foodie.app.controller.api;

import com.foodie.app.dto.AddToCartDto;
import com.foodie.app.dto.ApiResponse;
import com.foodie.app.dto.CartDto;
import com.foodie.app.dto.UpdateCartItemDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.CartService;
import com.foodie.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = getCustomer(userDetails);
        return ResponseEntity.ok(ApiResponse.ok(cartService.getCartDtoByCustomerId(customer.getId())));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> addItem(@AuthenticationPrincipal UserDetails userDetails,
                                                        @Valid @RequestBody AddToCartDto dto) {
        Customer customer = getCustomer(userDetails);
        return ResponseEntity.ok(ApiResponse.ok("Item added to cart", cartService.addItemToCart(customer.getId(), dto)));
    }

    @PutMapping("/items")
    public ResponseEntity<ApiResponse<CartDto>> updateItem(@AuthenticationPrincipal UserDetails userDetails,
                                                           @Valid @RequestBody UpdateCartItemDto dto) {
        Customer customer = getCustomer(userDetails);
        return ResponseEntity.ok(ApiResponse.ok("Cart item updated", cartService.updateCartItem(customer.getId(), dto)));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItem(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable("itemId") Long itemId) {
        Customer customer = getCustomer(userDetails);
        return ResponseEntity.ok(ApiResponse.ok("Item removed from cart", cartService.removeCartItem(customer.getId(), itemId)));
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }
}
