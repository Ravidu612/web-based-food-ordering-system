package com.foodie.app.controller;

import com.foodie.app.service.OrderService;
import com.foodie.app.service.ReportService;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final ReportService reportService;
    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("summary", reportService.getExecutiveDashboardSummary());
        model.addAttribute("recentOrders", orderService.getAllOrdersAdmin().stream().limit(5).toList());
        model.addAttribute("customers", userService.getAllCustomers());
        return "admin/dashboard";
    }
}
