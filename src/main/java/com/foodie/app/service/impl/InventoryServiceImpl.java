package com.foodie.app.service.impl;

import com.foodie.app.dto.RestockDto;
import com.foodie.app.entity.Inventory;
import com.foodie.app.entity.OrderItem;
import com.foodie.app.exception.InsufficientStockException;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.InventoryRepository;
import com.foodie.app.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory getInventoryByFoodId(Long foodId) {
        return inventoryRepository.findByFoodId(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory record not found for food id: " + foodId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAllWithFoodAndCategory();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventory> getLowStockAlerts() {
        return inventoryRepository.findLowStockItemsWithFoodAndCategory();
    }

    @Override
    @Transactional
    public Inventory restockFood(RestockDto dto) {
        Inventory inventory = getInventoryByFoodId(dto.getFoodId());
        inventory.setStockQuantity(inventory.getStockQuantity() + dto.getAdditionalQuantity());
        inventory.setLastRestockedAt(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    @Override
    public void verifyStockAvailability(Long foodId, int requestedQuantity) {
        Inventory inventory = getInventoryByFoodId(foodId);
        if (!inventory.hasSufficientStock(requestedQuantity)) {
            throw new InsufficientStockException("Insufficient stock for item: " + inventory.getFood().getName() +
                    ". Available stock: " + inventory.getStockQuantity() + ", requested: " + requestedQuantity);
        }
    }

    @Override
    @Transactional
    public void decrementStock(Long foodId, int quantity) {
        Inventory inventory = getInventoryByFoodId(foodId);
        if (inventory.getStockQuantity() < quantity) {
            throw new InsufficientStockException("Stock unavailable for " + inventory.getFood().getName());
        }
        inventory.setStockQuantity(inventory.getStockQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void restoreStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            Inventory inventory = getInventoryByFoodId(item.getFood().getId());
            inventory.setStockQuantity(inventory.getStockQuantity() + item.getQuantity());
            inventoryRepository.save(inventory);
        }
    }
}
