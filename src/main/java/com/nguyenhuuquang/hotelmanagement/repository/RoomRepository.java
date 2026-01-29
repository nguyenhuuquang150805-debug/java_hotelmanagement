package com.nguyenhuuquang.hotelmanagement.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

        Optional<Room> findByRoomNumber(String roomNumber);

        boolean existsByRoomNumber(String roomNumber);

        List<Room> findByStatus(RoomStatus status);

        List<Room> findByRoomTypeId(Long roomTypeId);

        /**
         * Tìm các phòng AVAILABLE (không bị đặt) trong khoảng thời gian
         * Phòng được coi là available nếu:
         * 1. Status = AVAILABLE
         * 2. KHÔNG có booking nào trong khoảng thời gian (checkIn -> checkOut)
         * với status IN (CONFIRMED, CHECKED_IN, PENDING)
         */
        @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' " +
                        "AND r.id NOT IN (" +
                        "  SELECT b.room.id FROM Booking b " +
                        "  WHERE b.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING') " +
                        "  AND (" +
                        "    (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)" +
                        "  )" +
                        ")")
        List<Room> findAvailableRooms(
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);

        /**
         * Tìm phòng available với các filter
         */
        @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' " +
                        "AND (:roomTypeId IS NULL OR r.roomType.id = :roomTypeId) " +
                        "AND (:minPrice IS NULL OR r.currentPrice >= :minPrice) " +
                        "AND (:maxPrice IS NULL OR r.currentPrice <= :maxPrice) " +
                        "AND (:isSmoking IS NULL OR r.isSmoking = :isSmoking) " +
                        "AND (:hasBalcony IS NULL OR r.hasBalcony = :hasBalcony) " +
                        "AND (:floorNumber IS NULL OR r.floorNumber = :floorNumber) " +
                        "AND r.id NOT IN (" +
                        "  SELECT b.room.id FROM Booking b " +
                        "  WHERE b.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING') " +
                        "  AND (" +
                        "    (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)" +
                        "  )" +
                        ")")
        List<Room> searchAvailableRooms(
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate,
                        @Param("roomTypeId") Long roomTypeId,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        @Param("isSmoking") Boolean isSmoking,
                        @Param("hasBalcony") Boolean hasBalcony,
                        @Param("floorNumber") Integer floorNumber);

        /**
         * Kiểm tra xem phòng có available trong khoảng thời gian không
         */
        @Query("SELECT CASE WHEN COUNT(b) > 0 THEN false ELSE true END " +
                        "FROM Booking b " +
                        "WHERE b.room.id = :roomId " +
                        "AND b.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING') " +
                        "AND (" +
                        "  (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)" +
                        ")")
        boolean isRoomAvailable(
                        @Param("roomId") Long roomId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);
}