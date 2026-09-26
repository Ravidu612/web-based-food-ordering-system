package com.foodie.app.controller.api;

import com.foodie.app.dto.ApiResponse;
import com.foodie.app.dto.CustomerRegistrationDto;
import com.foodie.app.entity.Customer;
import com.foodie.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> register(@Valid @RequestBody CustomerRegistrationDto dto) {
        Customer customer = userService.registerCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Customer registered successfully", customer));
    }
}
