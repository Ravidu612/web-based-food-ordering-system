package com.foodie.app.entity;

import com.foodie.app.entity.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by_admin_id", nullable = false)
    private Admin generatedByAdmin;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;

    @Column(name = "report_title", nullable = false, length = 200)
    private String reportTitle;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_revenue", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "total_expenses", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalExpenses = BigDecimal.ZERO;

    @Column(name = "net_profit", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal netProfit = BigDecimal.ZERO;

    @Column(name = "report_data_json", columnDefinition = "LONGTEXT")
    private String reportDataJson;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;
}
