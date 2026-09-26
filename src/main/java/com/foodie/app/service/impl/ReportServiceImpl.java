package com.foodie.app.service.impl;

import com.foodie.app.dto.ExpenseDto;
import com.foodie.app.dto.OrderResponseDto;
import com.foodie.app.dto.ProfitReportDto;
import com.foodie.app.dto.SalesReportDto;
import com.foodie.app.repository.*;
import com.foodie.app.service.ExpenseService;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SaleRepository saleRepository;
    private final ExpenseRepository expenseRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final InventoryRepository inventoryRepository;
    private final ExpenseService expenseService;
    private final OrderService orderService;

    @Override
    @Transactional(readOnly = true)
    public SalesReportDto generateSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<OrderResponseDto> ordersInRange = orderService.getAllOrdersAdmin().stream()
                .filter(o -> !o.getOrderDate().isBefore(startDateTime) && !o.getOrderDate().isAfter(endDateTime))
                .collect(Collectors.toList());

        BigDecimal totalRevenue = saleRepository.sumRevenueBetweenDates(startDate, endDate);
        BigDecimal totalCogs = saleRepository.sumCogsBetweenDates(startDate, endDate);
        BigDecimal grossMargin = totalRevenue.subtract(totalCogs);

        BigDecimal avgOrderValue = ordersInRange.isEmpty() ? BigDecimal.ZERO :
                totalRevenue.divide(BigDecimal.valueOf(ordersInRange.size()), 2, RoundingMode.HALF_UP);

        return SalesReportDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalGrossRevenue(totalRevenue)
                .totalCogs(totalCogs)
                .grossMargin(grossMargin)
                .totalOrdersCount(ordersInRange.size())
                .averageOrderValue(avgOrderValue)
                .orders(ordersInRange)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfitReportDto generateProfitReport(LocalDate startDate, LocalDate endDate) {
        BigDecimal totalRevenue = saleRepository.sumRevenueBetweenDates(startDate, endDate);
        BigDecimal totalCogs = saleRepository.sumCogsBetweenDates(startDate, endDate);
        BigDecimal totalOperatingExpenses = expenseRepository.sumExpensesBetweenDates(startDate, endDate);

        BigDecimal netProfit = totalRevenue.subtract(totalCogs).subtract(totalOperatingExpenses);

        BigDecimal profitMarginPercentage = BigDecimal.ZERO;
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            profitMarginPercentage = netProfit.divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        }

        List<ExpenseDto> expenses = expenseService.getExpensesBetween(startDate, endDate);

        return ProfitReportDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .totalCogs(totalCogs)
                .totalOperatingExpenses(totalOperatingExpenses)
                .netProfit(netProfit)
                .profitMarginPercentage(profitMarginPercentage)
                .isProfitable(netProfit.compareTo(BigDecimal.ZERO) >= 0)
                .expenseBreakdown(expenses)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getExecutiveDashboardSummary() {
        LocalDate today = LocalDate.now();
        BigDecimal todayRevenue = saleRepository.sumRevenueBetweenDates(today, today);
        BigDecimal monthRevenue = saleRepository.sumRevenueBetweenDates(today.withDayOfMonth(1), today);
        BigDecimal monthExpenses = expenseRepository.sumExpensesBetweenDates(today.withDayOfMonth(1), today);
        BigDecimal monthCogs = saleRepository.sumCogsBetweenDates(today.withDayOfMonth(1), today);
        BigDecimal monthNetProfit = monthRevenue.subtract(monthCogs).subtract(monthExpenses);

        long activeOrdersCount = orderRepository.countActiveOrders();
        long totalCustomersCount = customerRepository.count();
        int lowStockCount = inventoryRepository.findLowStockItems().size();

        Map<String, Object> summary = new HashMap<>();
        summary.put("todayRevenue", todayRevenue);
        summary.put("monthRevenue", monthRevenue);
        summary.put("monthExpenses", monthExpenses);
        summary.put("monthNetProfit", monthNetProfit);
        summary.put("activeOrdersCount", activeOrdersCount);
        summary.put("totalCustomersCount", totalCustomersCount);
        summary.put("lowStockCount", lowStockCount);

        return summary;
    }
}
