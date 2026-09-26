package com.foodie.app.service.impl;

import com.foodie.app.dto.FoodCategoryDto;
import com.foodie.app.entity.FoodCategory;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.FoodCategoryRepository;
import com.foodie.app.service.FoodCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodCategoryServiceImpl implements FoodCategoryService {

    private final FoodCategoryRepository categoryRepository;

    @Override
    public List<FoodCategory> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodCategoryDto> getAllCategoriesWithCount() {
        return categoryRepository.findAll().stream().map(cat -> FoodCategoryDto.builder()
                .id(cat.getId())
                .name(cat.getName())
                .description(cat.getDescription())
                .imageUrl(cat.getImageUrl())
                .isActive(cat.getIsActive())
                .foodCount(cat.getFoods() != null ? cat.getFoods().size() : 0)
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public FoodCategory getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public FoodCategory createCategory(FoodCategoryDto dto) {
        FoodCategory category = FoodCategory.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public FoodCategory updateCategory(Long id, FoodCategoryDto dto) {
        FoodCategory category = getCategoryById(id);
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setImageUrl(dto.getImageUrl());
        if (dto.getIsActive() != null) {
            category.setIsActive(dto.getIsActive());
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        FoodCategory category = getCategoryById(id);
        category.setIsActive(false); // Soft deactivation
        categoryRepository.save(category);
    }
}
