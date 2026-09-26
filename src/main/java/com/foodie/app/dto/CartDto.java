package com.foodie.app.dto;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private Long customerId;
    private String customerName;

    @Builder.Default
    private List<CartItemDto> items = new ArrayList<>();

    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal deliveryFee = BigDecimal.valueOf(3.50);

    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    private Integer totalItemCount;

    public void recalculateTotals() {
        this.subtotal = items.stream()
                .map(CartItemDto::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        this.totalItemCount = items.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();

        if (this.subtotal.compareTo(BigDecimal.ZERO) > 0) {
            this.tax = this.subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
            this.deliveryFee = BigDecimal.valueOf(3.50).setScale(2, RoundingMode.HALF_UP);
            this.total = this.subtotal.add(this.tax).add(this.deliveryFee).setScale(2, RoundingMode.HALF_UP);
        } else {
            this.tax = BigDecimal.ZERO;
            this.deliveryFee = BigDecimal.ZERO;
            this.total = BigDecimal.ZERO;
        }
    }
}
