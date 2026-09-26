package com.foodie.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Long id;
    private Long foodId;
    private String foodName;
    private String foodImageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private Integer availableStock;
}
