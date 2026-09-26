package com.foodie.app.dto;

import com.foodie.app.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderStatusDto {

    @NotNull(message = "Order status is required")
    private OrderStatus newStatus;

    private String cancellationReason;
}
