package com.foodie.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderDto {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    private String reason;
}
