package com.foodie.app.controller;

import com.foodie.app.entity.Customer;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final UserService userService;

    @GetMapping
    public String viewCustomers(Model model) {
        List<Customer> customers = userService.getAllCustomers();
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", customers.size());
        return "admin/customers";
    }

    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable("id") Long id,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam(value = "email", required = false) String email,
                                 @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
                                 @RequestParam(value = "city", required = false) String city,
                                 @RequestParam(value = "postalCode", required = false) String postalCode,
                                 @RequestParam(value = "defaultDeliveryAddress", required = false) String defaultDeliveryAddress,
                                 @RequestParam(value = "isEnabled", required = false, defaultValue = "true") boolean isEnabled,
                                 RedirectAttributes redirectAttributes) {
        try {
            Customer existingCustomer = userService.findCustomerById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));

            existingCustomer.setFirstName(firstName);
            existingCustomer.setLastName(lastName);
            if (phoneNumber != null) existingCustomer.setPhoneNumber(phoneNumber);
            if (city != null) existingCustomer.setCity(city);
            if (postalCode != null) existingCustomer.setPostalCode(postalCode);
            if (defaultDeliveryAddress != null) existingCustomer.setDefaultDeliveryAddress(defaultDeliveryAddress);

            if (existingCustomer.getUser() != null) {
                if (email != null && !email.trim().isEmpty()) {
                    existingCustomer.getUser().setEmail(email.trim());
                }
                existingCustomer.getUser().setIsEnabled(isEnabled);
            }

            userService.updateCustomer(existingCustomer);
            redirectAttributes.addFlashAttribute("successMessage", "Customer updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update customer: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteCustomer(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete customer. They might have dependent records.");
        }
        return "redirect:/admin/customers";
    }
}
