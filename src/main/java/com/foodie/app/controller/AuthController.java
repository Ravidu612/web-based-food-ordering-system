package com.foodie.app.controller;

import com.foodie.app.dto.CustomerRegistrationDto;
import com.foodie.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registrationDto")) {
            model.addAttribute("registrationDto", new CustomerRegistrationDto());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("registrationDto") CustomerRegistrationDto dto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            userService.registerCustomer(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! You can now log in.");
            return "redirect:/login";
        } catch (Exception ex) {
            bindingResult.rejectValue("email", "error.user", ex.getMessage());
            return "auth/register";
        }
    }
}
