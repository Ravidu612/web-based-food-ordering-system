package com.foodie.app.service;

import com.foodie.app.dto.AddToCartDto;
import com.foodie.app.dto.CartDto;
import com.foodie.app.dto.UpdateCartItemDto;
import com.foodie.app.entity.Cart;

public interface CartService {
    Cart getOrCreateCartForCustomer(Long customerId);
    CartDto getCartDtoByCustomerId(Long customerId);
    CartDto addItemToCart(Long customerId, AddToCartDto dto);
    CartDto updateCartItem(Long customerId, UpdateCartItemDto dto);
    CartDto removeCartItem(Long customerId, Long cartItemId);
    void clearCart(Long customerId);
}
