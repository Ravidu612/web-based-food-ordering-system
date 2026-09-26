package com.foodie.app.controller;

import com.foodie.app.dto.FoodDto;
import com.foodie.app.service.FoodCategoryService;
import com.foodie.app.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final FoodService foodService;
    private final FoodCategoryService categoryService;

    @GetMapping("/menu")
    public String viewMenu(@RequestParam(value = "categoryId", required = false) Long categoryId,
                           @RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "promotional", required = false) Boolean promotional,
                           Model model) {

        List<FoodDto> foods = foodService.filterFoods(categoryId, keyword, promotional);
        model.addAttribute("foods", foods);
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("promotionalOnly", promotional != null && promotional);

        return "customer/menu";
    }

    @GetMapping("/foods/{id}")
    public String viewFoodDetails(@PathVariable("id") Long id, Model model) {
        FoodDto food = foodService.getFoodDtoById(id);
        model.addAttribute("food", food);
        model.addAttribute("relatedFoods", foodService.getFoodsByCategory(food.getCategoryId()));
        return "customer/food-details";
    }
}
