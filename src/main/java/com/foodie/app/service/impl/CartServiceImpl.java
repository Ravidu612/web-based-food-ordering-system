package com.foodie.app.service.impl;

import com.foodie.app.dto.AddToCartDto;
import com.foodie.app.dto.CartDto;
import com.foodie.app.dto.CartItemDto;
import com.foodie.app.dto.UpdateCartItemDto;
import com.foodie.app.entity.Cart;
import com.foodie.app.entity.CartItem;
import com.foodie.app.entity.Customer;
import com.foodie.app.entity.Food;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.CartItemRepository;
import com.foodie.app.repository.CartRepository;
import com.foodie.app.repository.CustomerRepository;
import com.foodie.app.repository.FoodRepository;
import com.foodie.app.service.CartService;
import com.foodie.app.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final FoodRepository foodRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public Cart getOrCreateCartForCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .customer(customer)
                        .totalAmount(BigDecimal.ZERO)
                        .build()));
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartDtoByCustomerId(Long customerId) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long customerId, AddToCartDto dto) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        Food food = foodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException("Food not found with id: " + dto.getFoodId()));

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndFoodId(cart.getId(), food.getId());
        int targetQuantity = dto.getQuantity();
        if (existingItem.isPresent()) {
            targetQuantity += existingItem.get().getQuantity();
        }

        inventoryService.verifyStockAvailability(food.getId(), targetQuantity);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(targetQuantity);
            item.setUnitPrice(food.getEffectivePrice());
            item.calculateSubtotal();
            cartItemRepository.save(item);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .food(food)
                    .quantity(dto.getQuantity())
                    .unitPrice(food.getEffectivePrice())
                    .build();
            newItem.calculateSubtotal();
            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        cart.recalculateTotal();
        cartRepository.save(cart);

        return getCartDtoByCustomerId(customerId);
    }

    @Override
    @Transactional
    public CartDto updateCartItem(Long customerId, UpdateCartItemDto dto) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        CartItem item = cartItemRepository.findById(dto.getCartItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + dto.getCartItemId()));

        inventoryService.verifyStockAvailability(item.getFood().getId(), dto.getQuantity());

        item.setQuantity(dto.getQuantity());
        item.calculateSubtotal();
        cartItemRepository.save(item);

        cart.recalculateTotal();
        cartRepository.save(cart);

        return getCartDtoByCustomerId(customerId);
    }

    @Override
    @Transactional
    public CartDto removeCartItem(Long customerId, Long cartItemId) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        cartItemRepository.deleteById(cartItemId);

        cart.getCartItems().removeIf(item -> item.getId().equals(cartItemId));
        cart.recalculateTotal();
        cartRepository.save(cart);

        return getCartDtoByCustomerId(customerId);
    }

    @Override
    @Transactional
    public void clearCart(Long customerId) {
        Cart cart = getOrCreateCartForCustomer(customerId);
        cartItemRepository.deleteByCartId(cart.getId());
        cart.getCartItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private CartDto mapToDto(Cart cart) {
        CartDto dto = CartDto.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .customerName(cart.getCustomer().getFullName())
                .items(cart.getCartItems().stream().map(item -> CartItemDto.builder()
                        .id(item.getId())
                        .foodId(item.getFood().getId())
                        .foodName(item.getFood().getName())
                        .foodImageUrl(item.getFood().getImageUrl())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .availableStock(item.getFood().getInventory() != null ? item.getFood().getInventory().getStockQuantity() : 0)
                        .build()
                ).collect(Collectors.toList()))
                .build();

        dto.recalculateTotals();
        return dto;
    }
}
