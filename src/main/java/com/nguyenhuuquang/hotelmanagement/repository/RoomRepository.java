package com.nguyenhuuquang.hotelmanagement.repository;

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

    List<Room> findByRoomTypeId(Long roomTypeId);

    List<Room> findByStatus(RoomStatus status);

    List<Room> findByFloorNumber(Integer floorNumber);

    // Query phức tạp 1: Tìm phòng trống trong khoảng thời gian
    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT br.room.id FROM BookingRoom br " +
            "WHERE br.booking.checkInDate < :checkOut " +
            "AND br.booking.checkOutDate > :checkIn " +
            "AND br.booking.status NOT IN ('CANCELLED', 'NO_SHOW')" +
            ") AND r.status = 'AVAILABLE'")
    List<Room> findAvailableRooms(
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    // Query phức tạp 2: Thống kê phòng theo loại và trạng thái
    @Query("SELECT r.roomType.typeName, r.status, COUNT(r) " +
            "FROM Room r " +
            "GROUP BY r.roomType.typeName, r.status " +
            "ORDER BY r.roomType.typeName, r.status")
    List<Object[]> getRoomStatistics();

    // Query phức tạp 3: Tìm phòng theo nhiều tiêu chí
    @Query("SELECT r FROM Room r " +
            "WHERE (:typeId IS NULL OR r.roomType.id = :typeId) " +
            "AND (:floor IS NULL OR r.floorNumber = :floor) " +
            "AND (:minRating IS NULL OR r.rating >= :minRating) " +
            "AND r.status = 'AVAILABLE'")
    List<Room> searchRooms(
            @Param("typeId") Long typeId,
            @Param("floor") Integer floor,
            @Param("minRating") Double minRating);

    // Query phức tạp 4: Top phòng được đặt nhiều nhất
    @Query("SELECT r, COUNT(br) as bookingCount " +
            "FROM Room r " +
            "JOIN BookingRoom br ON br.room.id = r.id " +
            "GROUP BY r.id, r.roomNumber, r.roomType, r.floorNumber, r.viewDescription, " +
            "r.currentPrice, r.status, r.isSmoking, r.hasBalcony, r.rating, r.totalReviews " +
            "ORDER BY bookingCount DESC")
    List<Object[]> getTopBookedRooms();

    // Query phức tạp 5: Phòng theo mức giá
    @Query("SELECT " +
            "CASE " +
            "  WHEN r.currentPrice < 500000 THEN 'Dưới 500k' " +
            "  WHEN r.currentPrice < 1000000 THEN '500k-1tr' " +
            "  WHEN r.currentPrice < 2000000 THEN '1tr-2tr' " +
            "  ELSE 'Trên 2tr' " +
            "END as priceRange, " +
            "COUNT(r) as roomCount " +
            "FROM Room r " +
            "WHERE r.status = 'AVAILABLE' " +
            "GROUP BY priceRange " +
            "ORDER BY MIN(r.currentPrice)")
    List<Object[]> getRoomsByPriceRange();

    // Query phức tạp 6: Phòng có rating cao theo loại
    @Query("SELECT r.roomType.typeName, AVG(r.rating), COUNT(r) " +
            "FROM Room r " +
            "WHERE r.rating IS NOT NULL " +
            "GROUP BY r.roomType.typeName " +
            "HAVING AVG(r.rating) > :minAvgRating " +
            "ORDER BY AVG(r.rating) DESC")
    List<Object[]> getRoomTypesByRating(@Param("minAvgRating") Double minAvgRating);

    // Query phức tạp 7: Phòng chưa được đặt trong khoảng thời gian
    @Query("SELECT r FROM Room r " +
            "WHERE r.id NOT IN (" +
            "  SELECT DISTINCT br.room.id " +
            "  FROM BookingRoom br " +
            "  WHERE br.booking.checkInDate BETWEEN :startDate AND :endDate" +
            ") " +
            "AND r.status = 'AVAILABLE' " +
            "ORDER BY r.roomNumber")
    List<Room> findUnbookedRoomsInPeriod(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Query phức tạp 8: Thống kê chi tiết phòng
    @Query("SELECT r.floorNumber, " +
            "COUNT(r) as totalRooms, " +
            "SUM(CASE WHEN r.status = 'AVAILABLE' THEN 1 ELSE 0 END) as availableRooms, " +
            "AVG(r.currentPrice) as avgPrice " +
            "FROM Room r " +
            "GROUP BY r.floorNumber " +
            "ORDER BY r.floorNumber")
    List<Object[]> getFloorStatistics();
}