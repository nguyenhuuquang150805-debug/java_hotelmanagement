package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Warehouse;
import com.nguyenhuuquang.hotelmanagement.repository.WarehouseRepository;
import com.nguyenhuuquang.hotelmanagement.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Override
    @Transactional
    public Warehouse createWarehouseItem(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional
    public Warehouse updateWarehouseItem(Long id, Warehouse warehouse) {
        return warehouseRepository.findById(id)
                .map(existing -> {
                    warehouse.setId(id);
                    return warehouseRepository.save(warehouse);
                })
                .orElseThrow(() -> new RuntimeException("Warehouse item not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteWarehouseItem(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public Optional<Warehouse> getWarehouseItemById(Long id) {
        return warehouseRepository.findById(id);
    }

    @Override
    public Optional<Warehouse> getWarehouseItemByCode(String itemCode) {
        return warehouseRepository.findByItemCode(itemCode);
    }

    @Override
    public List<Warehouse> getAllWarehouseItems() {
        return warehouseRepository.findAll();
    }

    @Override
    public List<Warehouse> getWarehouseItemsByCategory(String category) {
        return warehouseRepository.findByCategory(category);
    }

    @Override
    public List<Warehouse> getActiveWarehouseItems() {
        return warehouseRepository.findByIsActive(true);
    }

    @Override
    public List<Warehouse> getLowStockItems() {
        return warehouseRepository.findLowStockItems();
    }

    @Override
    @Transactional
    public Warehouse updateQuantity(Long id, Integer quantity) {
        return warehouseRepository.findById(id)
                .map(warehouse -> {
                    warehouse.setQuantity(quantity);
                    warehouse.setLastRestocked(LocalDateTime.now());
                    return warehouseRepository.save(warehouse);
                })
                .orElseThrow(() -> new RuntimeException("Warehouse item not found with id: " + id));
    }

    @Override
    public List<Warehouse> searchWarehouseItemsByName(String itemName) {
        return warehouseRepository.findByItemNameContaining(itemName);
    }
}