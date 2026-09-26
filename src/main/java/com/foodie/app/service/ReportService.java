package com.foodie.app.service;

import com.foodie.app.dto.ProfitReportDto;
import com.foodie.app.dto.SalesReportDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ReportService {
    SalesReportDto generateSalesReport(LocalDate startDate, LocalDate endDate);
    ProfitReportDto generateProfitReport(LocalDate startDate, LocalDate endDate);
    Map<String, Object> getExecutiveDashboardSummary();
}
