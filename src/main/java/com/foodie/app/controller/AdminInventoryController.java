package com.foodie.app.controller;

import com.foodie.app.dto.RestockDto;
import com.foodie.app.entity.Inventory;
import com.foodie.app.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class AdminInventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public String viewInventory(Model model) {
        List<Inventory> inventoryList = inventoryService.getAllInventory();
        model.addAttribute("inventoryList", inventoryList);
        model.addAttribute("lowStockCount", inventoryService.getLowStockAlerts().size());
        return "admin/inventory";
    }

    @PostMapping("/restock")
    public String restock(@RequestParam("foodId") Long foodId,
                          @RequestParam("additionalQuantity") Integer additionalQuantity,
                          RedirectAttributes redirectAttributes) {
        try {
            inventoryService.restockFood(RestockDto.builder()
                    .foodId(foodId)
                    .additionalQuantity(additionalQuantity)
                    .build());
            redirectAttributes.addFlashAttribute("successMessage", "Stock successfully replenished!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/admin/inventory";
    }
}
