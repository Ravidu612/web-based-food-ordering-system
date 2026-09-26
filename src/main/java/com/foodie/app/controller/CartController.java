package com.foodie.app.controller;

import com.foodie.app.dto.AddToCartDto;
import com.foodie.app.dto.CartDto;
import com.foodie.app.dto.UpdateCartItemDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.CartService;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Customer customer = getCustomer(userDetails);
        CartDto cart = cartService.getCartDtoByCustomerId(customer.getId());
        model.addAttribute("cart", cart);
        return "customer/cart";
    }

    @PostMapping("/add")
    public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
                            @RequestParam("foodId") Long foodId,
                            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
                            RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        try {
            cartService.addItemToCart(customer.getId(), AddToCartDto.builder()
                    .foodId(foodId)
                    .quantity(quantity)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "Item added to cart successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam("cartItemId") Long cartItemId,
                                 @RequestParam("quantity") Integer quantity,
                                 RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        try {
            cartService.updateCartItem(customer.getId(), UpdateCartItemDto.builder()
                    .cartItemId(cartItemId)
                    .quantity(quantity)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable("itemId") Long itemId,
                             RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        cartService.removeCartItem(customer.getId(), itemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed from cart.");
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        cartService.clearCart(customer.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Cart cleared.");
        return "redirect:/cart";
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for user: " + userDetails.getUsername()));
    }
}
