package com.foodie.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false, unique = true)
    private Food food;

    @Column(name = "stock_quantity", nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(name = "low_stock_threshold", nullable = false)
    @Builder.Default
    private Integer lowStockThreshold = 10;

    @Column(name = "last_restocked_at")
    private LocalDateTime lastRestockedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public boolean isLowStock() {
        return stockQuantity != null && stockQuantity <= lowStockThreshold;
    }

    public boolean hasSufficientStock(int requestedQuantity) {
        return stockQuantity != null && stockQuantity >= requestedQuantity;
    }
}
