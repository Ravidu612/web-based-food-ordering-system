package com.foodie.app.repository;

import com.foodie.app.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByIsAvailableTrue();

    List<Food> findByCategoryIdAndIsAvailableTrue(Long categoryId);

    List<Food> findByIsPromotionalTrueAndIsAvailableTrue();

    @Query("SELECT f FROM Food f WHERE f.isAvailable = true AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Food> searchAvailableFoods(@Param("keyword") String keyword);

    @Query("SELECT f FROM Food f WHERE f.isAvailable = true AND " +
           "(:categoryId IS NULL OR f.category.id = :categoryId) AND " +
           "(:keyword IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:promotional IS NULL OR f.isPromotional = :promotional)")
    List<Food> filterFoods(@Param("categoryId") Long categoryId,
                           @Param("keyword") String keyword,
                           @Param("promotional") Boolean promotional);
}
