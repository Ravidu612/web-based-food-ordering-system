package com.foodie.app.controller;

import com.foodie.app.dto.CartDto;
import com.foodie.app.dto.CheckoutDto;
import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.CartService;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String showCheckoutPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Customer customer = getCustomer(userDetails);
        CartDto cart = cartService.getCartDtoByCustomerId(customer.getId());

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        if (!model.containsAttribute("checkoutDto")) {
            CheckoutDto checkoutDto = CheckoutDto.builder()
                    .deliveryAddress(customer.getDefaultDeliveryAddress())
                    .contactPhone(customer.getPhoneNumber())
                    .cardHolderName(customer.getFullName())
                    .build();
            model.addAttribute("checkoutDto", checkoutDto);
        }

        model.addAttribute("cart", cart);
        model.addAttribute("customer", customer);
        return "customer/checkout";
    }

    @PostMapping("/place-order")
    public String processCheckout(@AuthenticationPrincipal UserDetails userDetails,
                                  @Valid @ModelAttribute("checkoutDto") CheckoutDto checkoutDto,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        CartDto cart = cartService.getCartDtoByCustomerId(customer.getId());

        if (cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            model.addAttribute("customer", customer);
            return "customer/checkout";
        }

        try {
            OrderResponseDto order = orderService.processOrderCheckout(customer.getId(), checkoutDto);
            return "redirect:/orders/confirmation/" + order.getId();
        } catch (Exception ex) {
            model.addAttribute("cart", cart);
            model.addAttribute("customer", customer);
            model.addAttribute("errorMessage", ex.getMessage());
            return "customer/checkout";
        }
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + userDetails.getUsername()));
    }
}
