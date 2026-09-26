package com.foodie.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "total_revenue", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "total_cogs", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalCogs = BigDecimal.ZERO;

    @Column(name = "net_margin", nullable = false, precision = 10, scale = 2)
    private BigDecimal netMargin;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
