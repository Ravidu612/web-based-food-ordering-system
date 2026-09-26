package com.foodie.app.repository;

import com.foodie.app.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByFoodId(Long foodId);

    @Query("SELECT DISTINCT i FROM Inventory i JOIN FETCH i.food f LEFT JOIN FETCH f.category")
    List<Inventory> findAllWithFoodAndCategory();

    @Query("SELECT DISTINCT i FROM Inventory i JOIN FETCH i.food f LEFT JOIN FETCH f.category WHERE i.stockQuantity <= i.lowStockThreshold")
    List<Inventory> findLowStockItemsWithFoodAndCategory();

    @Query("SELECT i FROM Inventory i WHERE i.stockQuantity <= i.lowStockThreshold")
    List<Inventory> findLowStockItems();
}
