package com.nguyenhuuquang.hotelmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByItemCode(String itemCode);

    List<Warehouse> findByCategory(String category);

    List<Warehouse> findByIsActive(Boolean isActive);

    @Query("SELECT w FROM Warehouse w WHERE w.quantity < w.minQuantity")
    List<Warehouse> findLowStockItems();

    List<Warehouse> findByItemNameContaining(String itemName);
}