package com.foodie.app.controller;

import com.foodie.app.dto.ExpenseDto;
import com.foodie.app.dto.ProfitReportDto;
import com.foodie.app.dto.SalesReportDto;
import com.foodie.app.entity.enums.ExpenseCategory;
import com.foodie.app.service.ExpenseService;
import com.foodie.app.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;
    private final ExpenseService expenseService;

    @GetMapping("/sales")
    public String salesReport(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                              @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                              Model model) {
        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = LocalDate.now();

        SalesReportDto report = reportService.generateSalesReport(startDate, endDate);
        model.addAttribute("report", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin/sales-report";
    }

    @GetMapping("/profit")
    public String profitReport(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                               Model model) {
        if (startDate == null) startDate = LocalDate.now().withDayOfMonth(1);
        if (endDate == null) endDate = LocalDate.now();

        ProfitReportDto report = reportService.generateProfitReport(startDate, endDate);
        model.addAttribute("report", report);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "admin/profit-report";
    }

    @GetMapping("/expenses")
    public String viewExpenses(Model model) {
        var expenses = expenseService.getAllExpenses();
        java.math.BigDecimal ingredients = java.math.BigDecimal.ZERO;
        java.math.BigDecimal labor = java.math.BigDecimal.ZERO;
        java.math.BigDecimal utilities = java.math.BigDecimal.ZERO;
        java.math.BigDecimal other = java.math.BigDecimal.ZERO;
        java.math.BigDecimal total = java.math.BigDecimal.ZERO;

        for (var e : expenses) {
            if (e.getAmount() != null) {
                total = total.add(e.getAmount());
                if (e.getExpenseCategory() != null) {
                    switch (e.getExpenseCategory()) {
                        case RAW_INGREDIENTS -> ingredients = ingredients.add(e.getAmount());
                        case SALARIES -> labor = labor.add(e.getAmount());
                        case UTILITIES -> utilities = utilities.add(e.getAmount());
                        default -> other = other.add(e.getAmount());
                    }
                }
            }
        }

        java.util.Map<String, Object> summary = new java.util.HashMap<>();
        summary.put("ingredients", ingredients);
        summary.put("labor", labor);
        summary.put("utilities", utilities);
        summary.put("other", other);
        summary.put("total", total);

        model.addAttribute("expenses", expenses);
        model.addAttribute("expenseSummary", summary);
        model.addAttribute("categories", ExpenseCategory.values());
        if (!model.containsAttribute("expenseDto")) {
            ExpenseDto dto = new ExpenseDto();
            dto.setExpenseDate(LocalDate.now());
            model.addAttribute("expenseDto", dto);
        }
        return "admin/expenses";
    }

    @PostMapping("/expenses/add")
    public String addExpense(@AuthenticationPrincipal UserDetails userDetails,
                             @Valid @ModelAttribute("expenseDto") ExpenseDto dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed for expense voucher.");
            return "redirect:/admin/reports/expenses";
        }
        expenseService.createExpense(dto, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "Expense record logged successfully!");
        return "redirect:/admin/reports/expenses";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        expenseService.deleteExpense(id);
        redirectAttributes.addFlashAttribute("successMessage", "Expense record deleted.");
        return "redirect:/admin/reports/expenses";
    }

    @PostMapping("/expenses/update/{id}")
    public String updateExpense(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("expenseDto") ExpenseDto dto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed for expense update.");
            return "redirect:/admin/reports/expenses";
        }
        expenseService.updateExpense(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Expense record updated successfully!");
        return "redirect:/admin/reports/expenses";
    }
}
