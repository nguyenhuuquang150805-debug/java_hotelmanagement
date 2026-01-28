package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Warehouse;

public interface WarehouseService {
    Warehouse createWarehouseItem(Warehouse warehouse);

    Warehouse updateWarehouseItem(Long id, Warehouse warehouse);

    void deleteWarehouseItem(Long id);

    Optional<Warehouse> getWarehouseItemById(Long id);

    Optional<Warehouse> getWarehouseItemByCode(String itemCode);

    List<Warehouse> getAllWarehouseItems();

    List<Warehouse> getWarehouseItemsByCategory(String category);

    List<Warehouse> getActiveWarehouseItems();

    List<Warehouse> getLowStockItems();

    Warehouse updateQuantity(Long id, Integer quantity);

    List<Warehouse> searchWarehouseItemsByName(String itemName);
}