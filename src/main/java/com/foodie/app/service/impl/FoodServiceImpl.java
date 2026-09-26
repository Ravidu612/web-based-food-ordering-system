package com.foodie.app.service.impl;

import com.foodie.app.dto.FoodDto;
import com.foodie.app.entity.Food;
import com.foodie.app.entity.FoodCategory;
import com.foodie.app.entity.Inventory;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.FoodCategoryRepository;
import com.foodie.app.repository.FoodRepository;
import com.foodie.app.repository.InventoryRepository;
import com.foodie.app.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final FoodCategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> getAllAvailableFoods() {
        return foodRepository.findByIsAvailableTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> getFoodsByCategory(Long categoryId) {
        return foodRepository.findByCategoryIdAndIsAvailableTrue(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> getPromotionalFoods() {
        return foodRepository.findByIsPromotionalTrueAndIsAvailableTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> searchFoods(String keyword) {
        return foodRepository.searchAvailableFoods(keyword).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> filterFoods(Long categoryId, String keyword, Boolean promotional) {
        return foodRepository.filterFoods(categoryId, keyword, promotional).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FoodDto getFoodDtoById(Long id) {
        return mapToDto(getFoodById(id));
    }

    @Override
    public Food getFoodById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found with id: " + id));
    }

    @Override
    @Transactional
    public Food createFood(FoodDto dto) {
        FoodCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        Food food = Food.builder()
                .category(category)
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .costPrice(dto.getCostPrice() != null ? dto.getCostPrice() : BigDecimal.ZERO)
                .prepTimeMinutes(dto.getPrepTimeMinutes() != null ? dto.getPrepTimeMinutes() : 15)
                .imageUrl(dto.getImageUrl())
                .isAvailable(dto.getIsAvailable() != null ? dto.getIsAvailable() : true)
                .isPromotional(dto.getIsPromotional() != null ? dto.getIsPromotional() : false)
                .discountPercentage(dto.getDiscountPercentage() != null ? dto.getDiscountPercentage() : BigDecimal.ZERO)
                .build();

        Food savedFood = foodRepository.save(food);

        Inventory inventory = Inventory.builder()
                .food(savedFood)
                .stockQuantity(dto.getStockQuantity() != null ? dto.getStockQuantity() : 50)
                .lowStockThreshold(10)
                .lastRestockedAt(LocalDateTime.now())
                .build();
        inventoryRepository.save(inventory);

        return savedFood;
    }

    @Override
    @Transactional
    public Food updateFood(Long id, FoodDto dto) {
        Food food = getFoodById(id);
        FoodCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        food.setCategory(category);
        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setPrice(dto.getPrice());
        if (dto.getCostPrice() != null) food.setCostPrice(dto.getCostPrice());
        if (dto.getPrepTimeMinutes() != null) food.setPrepTimeMinutes(dto.getPrepTimeMinutes());
        if (dto.getImageUrl() != null) food.setImageUrl(dto.getImageUrl());
        if (dto.getIsAvailable() != null) food.setIsAvailable(dto.getIsAvailable());
        if (dto.getIsPromotional() != null) food.setIsPromotional(dto.getIsPromotional());
        if (dto.getDiscountPercentage() != null) food.setDiscountPercentage(dto.getDiscountPercentage());

        if (dto.getStockQuantity() != null && food.getInventory() != null) {
            food.getInventory().setStockQuantity(dto.getStockQuantity());
            inventoryRepository.save(food.getInventory());
        }

        return foodRepository.save(food);
    }

    @Override
    @Transactional
    public void toggleAvailability(Long id) {
        Food food = getFoodById(id);
        food.setIsAvailable(!food.getIsAvailable());
        foodRepository.save(food);
    }

    @Override
    @Transactional
    public void deleteFood(Long id) {
        Food food = getFoodById(id);
        food.setIsAvailable(false);
        foodRepository.save(food);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodDto> getAllFoodsAdmin() {
        return foodRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private FoodDto mapToDto(Food food) {
        int stock = food.getInventory() != null ? food.getInventory().getStockQuantity() : 0;
        boolean lowStock = food.getInventory() != null && food.getInventory().isLowStock();

        return FoodDto.builder()
                .id(food.getId())
                .categoryId(food.getCategory().getId())
                .categoryName(food.getCategory().getName())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .costPrice(food.getCostPrice())
                .prepTimeMinutes(food.getPrepTimeMinutes())
                .imageUrl(food.getImageUrl())
                .isAvailable(food.getIsAvailable())
                .isPromotional(food.getIsPromotional())
                .discountPercentage(food.getDiscountPercentage())
                .effectivePrice(food.getEffectivePrice())
                .stockQuantity(stock)
                .isLowStock(lowStock)
                .build();
    }
}
