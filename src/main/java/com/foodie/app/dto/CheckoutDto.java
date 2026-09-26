package com.foodie.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutDto {

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotBlank(message = "Contact phone number is required")
    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "Please provide a valid phone number")
    private String contactPhone;

    private String deliveryNotes;

    // Simulated Payment Parameters
    @NotBlank(message = "Cardholder name is required")
    private String cardHolderName;

    @NotBlank(message = "16-digit card number is required")
    @Pattern(regexp = "^[0-9]{16}$", message = "Card number must be 16 numeric digits")
    private String cardNumber;

    @NotBlank(message = "Expiration date is required (MM/YY)")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "Expiry must be in MM/YY format")
    private String cardExpiry;

    @NotBlank(message = "3-digit CVV is required")
    @Pattern(regexp = "^[0-9]{3}$", message = "CVV must be 3 digits")
    private String cardCvv;
}
