package com.foodie.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRegistrationDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Email address is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+0-9\\s-]{8,20}$", message = "Please provide a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Delivery address is required")
    private String defaultDeliveryAddress;

    @NotBlank(message = "City is required")
    private String city;

    private String postalCode;
}
