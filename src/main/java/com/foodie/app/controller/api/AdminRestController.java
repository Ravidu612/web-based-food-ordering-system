package com.foodie.app.controller.api;

import com.foodie.app.dto.*;
import com.foodie.app.entity.Inventory;
import com.foodie.app.service.ExpenseService;
import com.foodie.app.service.InventoryService;
import com.foodie.app.service.OrderService;
import com.foodie.app.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ReportService reportService;
    private final ExpenseService expenseService;

    @GetMapping("/dashboard/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSummary() {
        return ResponseEntity.ok(ApiResponse.ok(reportService.getExecutiveDashboardSummary()));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<List<Inventory>>> getInventory() {
        return ResponseEntity.ok(ApiResponse.ok(inventoryService.getAllInventory()));
    }

    @PostMapping("/inventory/restock")
    public ResponseEntity<ApiResponse<Inventory>> restock(@Valid @RequestBody RestockDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Stock replenished", inventoryService.restockFood(dto)));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponseDto>> updateStatus(@PathVariable("id") Long id,
                                                                      @Valid @RequestBody UpdateOrderStatusDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Status updated", orderService.updateOrderStatus(id, dto.getNewStatus(), dto.getCancellationReason())));
    }

    @PostMapping("/expenses")
    public ResponseEntity<ApiResponse<Object>> addExpense(@AuthenticationPrincipal UserDetails userDetails,
                                                          @Valid @RequestBody ExpenseDto dto) {
        return ResponseEntity.ok(ApiResponse.ok("Expense recorded", expenseService.createExpense(dto, userDetails.getUsername())));
    }

    @GetMapping("/reports/sales")
    public ResponseEntity<ApiResponse<SalesReportDto>> getSalesReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.generateSalesReport(startDate, endDate)));
    }

    @GetMapping("/reports/profit")
    public ResponseEntity<ApiResponse<ProfitReportDto>> getProfitReport(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(ApiResponse.ok(reportService.generateProfitReport(startDate, endDate)));
    }
}
