package com.foodie.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestockDto {

    @NotNull(message = "Food ID is required")
    private Long foodId;

    @NotNull(message = "Quantity to add is required")
    @Min(value = 1, message = "Quantity to add must be at least 1")
    private Integer additionalQuantity;
}
