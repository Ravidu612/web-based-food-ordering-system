package com.foodie.app.controller;

import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping
    public String viewOrderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Customer customer = getCustomer(userDetails);
        List<OrderResponseDto> orders = orderService.getCustomerOrders(customer.getId());
        model.addAttribute("orders", orders);
        return "customer/orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@AuthenticationPrincipal UserDetails userDetails,
                                   @PathVariable("id") Long id,
                                   Model model) {
        Customer customer = getCustomer(userDetails);
        OrderResponseDto order = orderService.getOrderResponseDtoById(id);

        if (!order.getCustomerId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "customer/order-details";
    }

    @GetMapping("/confirmation/{id}")
    public String showOrderConfirmation(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable("id") Long id,
                                        Model model) {
        Customer customer = getCustomer(userDetails);
        OrderResponseDto order = orderService.getOrderResponseDtoById(id);

        if (!order.getCustomerId().equals(customer.getId())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "customer/order-confirmation";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable("id") Long id,
                              @RequestParam(value = "reason", required = false) String reason,
                              RedirectAttributes redirectAttributes) {
        Customer customer = getCustomer(userDetails);
        try {
            orderService.cancelOrder(id, customer.getId(), reason);
            redirectAttributes.addFlashAttribute("successMessage", "Order #" + id + " has been successfully cancelled and stock restored.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/orders/" + id;
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + userDetails.getUsername()));
    }
}
