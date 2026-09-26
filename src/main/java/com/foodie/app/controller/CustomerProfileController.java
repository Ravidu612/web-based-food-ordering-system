package com.foodie.app.controller;

import com.foodie.app.entity.Customer;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final UserService userService;

    @GetMapping
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Customer customer = getCustomer(userDetails);
        model.addAttribute("customer", customer);
        model.addAttribute("email", userDetails.getUsername());
        return "customer/profile";
    }

    @PostMapping
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("defaultDeliveryAddress") String address,
                                @RequestParam("city") String city,
                                @RequestParam(value = "postalCode", required = false) String postalCode,
                                RedirectAttributes redirectAttributes) {
        try {
            Customer customer = getCustomer(userDetails);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPhoneNumber(phoneNumber);
            customer.setDefaultDeliveryAddress(address);
            customer.setCity(city);
            customer.setPostalCode(postalCode);
            userService.updateCustomer(customer);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/customer/profile";
    }

    private Customer getCustomer(UserDetails userDetails) {
        return userService.findCustomerByUserEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for: " + userDetails.getUsername()));
    }
}
