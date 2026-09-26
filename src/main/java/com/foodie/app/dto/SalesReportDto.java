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
public class SalesReportDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalGrossRevenue;
    private BigDecimal totalCogs;
    private BigDecimal grossMargin;
    private Integer totalOrdersCount;
    private BigDecimal averageOrderValue;

    @Builder.Default
    private List<OrderResponseDto> orders = new ArrayList<>();
}
