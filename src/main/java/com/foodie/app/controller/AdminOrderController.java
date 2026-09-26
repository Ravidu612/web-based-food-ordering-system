package com.foodie.app.controller;

import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.entity.enums.OrderStatus;
import com.foodie.app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String viewOrdersQueue(Model model) {
        List<OrderResponseDto> orders = orderService.getAllOrdersAdmin();
        model.addAttribute("orders", orders);
        model.addAttribute("allStatuses", OrderStatus.values());
        return "admin/orders";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam("orderId") Long orderId,
                               @RequestParam("newStatus") OrderStatus newStatus,
                               @RequestParam(value = "cancellationReason", required = false) String cancellationReason,
                               RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, newStatus, cancellationReason);
            redirectAttributes.addFlashAttribute("successMessage", "Order #" + orderId + " updated to " + newStatus.name());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
