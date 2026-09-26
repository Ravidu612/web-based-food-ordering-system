package com.foodie.app.controller;

import com.foodie.app.service.FoodCategoryService;
import com.foodie.app.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FoodCategoryService categoryService;
    private final FoodService foodService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", categoryService.getAllCategoriesWithCount());
        model.addAttribute("promotionalFoods", foodService.getPromotionalFoods());
        model.addAttribute("featuredFoods", foodService.getAllAvailableFoods());
        return "customer/index";
    }

    @GetMapping("/about")
    public String about() {
        return "customer/about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "customer/contact";
    }
}
