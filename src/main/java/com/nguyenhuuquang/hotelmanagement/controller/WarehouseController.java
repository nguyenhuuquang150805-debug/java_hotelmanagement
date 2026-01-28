package com.nguyenhuuquang.hotelmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.Warehouse;
import com.nguyenhuuquang.hotelmanagement.service.WarehouseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/warehouse")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouseItem(@RequestBody Warehouse warehouse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouseItem(warehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouseItem(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouseItem(id, warehouse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseItem(@PathVariable Long id) {
        warehouseService.deleteWarehouseItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouseItemById(@PathVariable Long id) {
        return warehouseService.getWarehouseItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Warehouse> getWarehouseItemByCode(@PathVariable String code) {
        return warehouseService.getWarehouseItemByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Warehouse>> getAllWarehouseItems() {
        return ResponseEntity.ok(warehouseService.getAllWarehouseItems());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Warehouse>> getWarehouseItemsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(warehouseService.getWarehouseItemsByCategory(category));
    }

    @GetMapping("/active")
    public ResponseEntity<List<Warehouse>> getActiveWarehouseItems() {
        return ResponseEntity.ok(warehouseService.getActiveWarehouseItems());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Warehouse>> getLowStockItems() {
        return ResponseEntity.ok(warehouseService.getLowStockItems());
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Warehouse> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(warehouseService.updateQuantity(id, quantity));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Warehouse>> searchWarehouseItems(@RequestParam String name) {
        return ResponseEntity.ok(warehouseService.searchWarehouseItemsByName(name));
    }
}