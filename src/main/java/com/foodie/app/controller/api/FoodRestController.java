package com.foodie.app.controller.api;

import com.foodie.app.dto.ApiResponse;
import com.foodie.app.dto.FoodCategoryDto;
import com.foodie.app.dto.FoodDto;
import com.foodie.app.entity.Food;
import com.foodie.app.service.FoodCategoryService;
import com.foodie.app.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FoodRestController {

    private final FoodService foodService;
    private final FoodCategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<FoodCategoryDto>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok(categoryService.getAllCategoriesWithCount()));
    }

    @GetMapping("/foods")
    public ResponseEntity<ApiResponse<List<FoodDto>>> getFoods(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                               @RequestParam(value = "keyword", required = false) String keyword,
                                                               @RequestParam(value = "promotional", required = false) Boolean promotional) {
        return ResponseEntity.ok(ApiResponse.ok(foodService.filterFoods(categoryId, keyword, promotional)));
    }

    @GetMapping("/foods/{id}")
    public ResponseEntity<ApiResponse<FoodDto>> getFoodById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.ok(foodService.getFoodDtoById(id)));
    }

    @PostMapping("/admin/foods")
    public ResponseEntity<ApiResponse<Food>> createFood(@Valid @RequestBody FoodDto dto) {
        Food food = foodService.createFood(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Food dish created", food));
    }
}
