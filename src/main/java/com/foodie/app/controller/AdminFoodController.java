package com.foodie.app.controller;

import com.foodie.app.dto.FoodCategoryDto;
import com.foodie.app.dto.FoodDto;
import com.foodie.app.service.FoodCategoryService;
import com.foodie.app.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;
    private final FoodCategoryService categoryService;

    // --- FOOD CATEGORIES ---
    @GetMapping("/categories")
    public String viewCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategoriesWithCount());
        if (!model.containsAttribute("categoryDto")) {
            model.addAttribute("categoryDto", new FoodCategoryDto());
        }
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@Valid @ModelAttribute("categoryDto") FoodCategoryDto dto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed for category creation.");
            return "redirect:/admin/categories";
        }
        categoryService.createCategory(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
        return "redirect:/admin/categories";
    }

    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable("id") Long id,
                                 @ModelAttribute FoodCategoryDto dto,
                                 RedirectAttributes redirectAttributes) {
        if (dto.getIsActive() == null) {
            dto.setIsActive(false);
        }
        categoryService.updateCategory(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Category updated successfully!");
        return "redirect:/admin/categories";
    }

    // --- FOOD ITEMS ---
    @GetMapping("/foods")
    public String viewFoods(Model model) {
        model.addAttribute("foods", foodService.getAllFoodsAdmin());
        model.addAttribute("categories", categoryService.getAllActiveCategories());
        if (!model.containsAttribute("foodDto")) {
            model.addAttribute("foodDto", new FoodDto());
        }
        return "admin/foods";
    }

    @PostMapping("/foods/add")
    public String addFood(@Valid @ModelAttribute("foodDto") FoodDto dto,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation failed for food item creation.");
            return "redirect:/admin/foods";
        }
        if (dto.getIsAvailable() == null) {
            dto.setIsAvailable(true);
        }
        if (dto.getIsPromotional() == null) {
            dto.setIsPromotional(false);
        }
        foodService.createFood(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Food dish added successfully!");
        return "redirect:/admin/foods";
    }

    @PostMapping("/foods/update/{id}")
    public String updateFood(@PathVariable("id") Long id,
                             @ModelAttribute FoodDto dto,
                             RedirectAttributes redirectAttributes) {
        if (dto.getIsAvailable() == null) {
            dto.setIsAvailable(false);
        }
        if (dto.getIsPromotional() == null) {
            dto.setIsPromotional(false);
        }
        foodService.updateFood(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Food item updated successfully!");
        return "redirect:/admin/foods";
    }

    @PostMapping("/foods/toggle/{id}")
    public String toggleAvailability(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        foodService.toggleAvailability(id);
        redirectAttributes.addFlashAttribute("successMessage", "Food item availability toggled.");
        return "redirect:/admin/foods";
    }

    @PostMapping("/foods/delete/{id}")
    public String deleteFood(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        foodService.deleteFood(id);
        redirectAttributes.addFlashAttribute("successMessage", "Food item deleted (deactivated) successfully.");
        return "redirect:/admin/foods";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "Category deleted (deactivated) successfully.");
        return "redirect:/admin/categories";
    }
}
