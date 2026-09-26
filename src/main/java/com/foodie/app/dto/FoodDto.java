package com.foodie.app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodDto {
    private Long id;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private String categoryName;

    @NotBlank(message = "Food name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero")
    private BigDecimal price;

    private BigDecimal costPrice;

    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    private Integer prepTimeMinutes;

    private String imageUrl;
    private Boolean isAvailable;
    private Boolean isPromotional;
    private BigDecimal discountPercentage;
    private BigDecimal effectivePrice;
    private Integer stockQuantity;
    private Boolean isLowStock;
}
