package com.foodie.app.repository;

import com.foodie.app.entity.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    List<FoodCategory> findByIsActiveTrue();
    Optional<FoodCategory> findByNameIgnoreCase(String name);
}
