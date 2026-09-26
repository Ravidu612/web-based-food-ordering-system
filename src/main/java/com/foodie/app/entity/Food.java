package com.foodie.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "foods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FoodCategory category;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal costPrice = BigDecimal.ZERO;

    @Column(name = "prep_time_minutes", nullable = false)
    @Builder.Default
    private Integer prepTimeMinutes = 15;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    @Column(name = "is_promotional", nullable = false)
    @Builder.Default
    private Boolean isPromotional = false;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @OneToOne(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Inventory inventory;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal getEffectivePrice() {
        if (isPromotional && discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountFactor = BigDecimal.ONE.subtract(discountPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
            return price.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
        }
        return price.setScale(2, RoundingMode.HALF_UP);
    }
}
