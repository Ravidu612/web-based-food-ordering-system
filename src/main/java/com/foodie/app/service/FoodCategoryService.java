package com.foodie.app.service;

import com.foodie.app.dto.FoodCategoryDto;
import com.foodie.app.entity.FoodCategory;

import java.util.List;

public interface FoodCategoryService {
    List<FoodCategory> getAllActiveCategories();
    List<FoodCategoryDto> getAllCategoriesWithCount();
    FoodCategory getCategoryById(Long id);
    FoodCategory createCategory(FoodCategoryDto categoryDto);
    FoodCategory updateCategory(Long id, FoodCategoryDto categoryDto);
    void deleteCategory(Long id);
}
