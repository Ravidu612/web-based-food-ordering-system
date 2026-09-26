package com.foodie.app.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfitReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalRevenue;
    private BigDecimal totalCogs;
    private BigDecimal totalOperatingExpenses;
    private BigDecimal netProfit;
    private BigDecimal profitMarginPercentage;
    private Boolean isProfitable;

    @Builder.Default
    private List<ExpenseDto> expenseBreakdown = new ArrayList<>();
}
