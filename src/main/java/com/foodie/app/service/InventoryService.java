package com.foodie.app.service;

import com.foodie.app.dto.RestockDto;
import com.foodie.app.entity.Inventory;
import com.foodie.app.entity.OrderItem;

import java.util.List;

public interface InventoryService {
    Inventory getInventoryByFoodId(Long foodId);
    List<Inventory> getAllInventory();
    List<Inventory> getLowStockAlerts();
    Inventory restockFood(RestockDto restockDto);
    void verifyStockAvailability(Long foodId, int requestedQuantity);
    void decrementStock(Long foodId, int quantity);
    void restoreStock(List<OrderItem> orderItems);
}
