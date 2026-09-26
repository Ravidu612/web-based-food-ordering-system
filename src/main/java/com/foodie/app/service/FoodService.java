package com.foodie.app.service;

import com.foodie.app.dto.FoodDto;
import com.foodie.app.entity.Food;

import java.util.List;

public interface FoodService {
    List<FoodDto> getAllAvailableFoods();
    List<FoodDto> getFoodsByCategory(Long categoryId);
    List<FoodDto> getPromotionalFoods();
    List<FoodDto> searchFoods(String keyword);
    List<FoodDto> filterFoods(Long categoryId, String keyword, Boolean promotional);
    FoodDto getFoodDtoById(Long id);
    Food getFoodById(Long id);
    Food createFood(FoodDto foodDto);
    Food updateFood(Long id, FoodDto foodDto);
    void toggleAvailability(Long id);
    void deleteFood(Long id);
    List<FoodDto> getAllFoodsAdmin();
}
