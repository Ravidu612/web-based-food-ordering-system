package com.foodie.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodCategoryDto {
    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
    private String imageUrl;
    private Boolean isActive;
    private Integer foodCount;
}
