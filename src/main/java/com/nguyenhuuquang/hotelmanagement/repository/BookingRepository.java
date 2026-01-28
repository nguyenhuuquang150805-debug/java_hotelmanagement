package com.nguyenhuuquang.hotelmanagement.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
        // ✅ Query cơ bản
        Optional<Booking> findByBookingCode(String bookingCode);

        List<Booking> findByCustomerId(Long customerId);

        List<Booking> findByStatus(BookingStatus status);

        List<Booking> findByCheckInDateBetween(LocalDate start, LocalDate end);

        List<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status);

        // ✅ NEW: Kiểm tra booking trùng phòng trong khoảng thời gian
        @Query("SELECT b FROM Booking b " +
                        "JOIN b.bookingRooms br " +
                        "WHERE br.room.id = :roomId " +
                        "AND b.status NOT IN ('CANCELLED', 'NO_SHOW') " +
                        "AND ((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn))")
        List<Booking> findOverlappingBookings(
                        @Param("roomId") Long roomId,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut);

        // ✅ NEW: Kiểm tra overlap cho nhiều phòng
        @Query("SELECT DISTINCT br.room.id FROM Booking b " +
                        "JOIN b.bookingRooms br " +
                        "WHERE br.room.id IN :roomIds " +
                        "AND b.status NOT IN ('CANCELLED', 'NO_SHOW') " +
                        "AND ((b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn))")
        List<Long> findOccupiedRoomIds(
                        @Param("roomIds") List<Long> roomIds,
                        @Param("checkIn") LocalDate checkIn,
                        @Param("checkOut") LocalDate checkOut);

        // ✅ Query phức tạp 1: Thống kê doanh thu theo tháng
        @Query("SELECT MONTH(b.checkInDate) as month, SUM(b.finalAmount) as revenue " +
                        "FROM Booking b " +
                        "WHERE YEAR(b.checkInDate) = :year AND b.status = 'CONFIRMED' " +
                        "GROUP BY MONTH(b.checkInDate) " +
                        "ORDER BY month")
        List<Object[]> getMonthlyRevenue(@Param("year") int year);

        // ✅ Query phức tạp 2: Tìm booking với nhiều điều kiện
        @Query("SELECT b FROM Booking b " +
                        "WHERE b.checkInDate BETWEEN :startDate AND :endDate " +
                        "AND b.status IN :statuses " +
                        "AND b.finalAmount >= :minAmount " +
                        "ORDER BY b.checkInDate DESC")
        List<Booking> findComplexBookings(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("statuses") List<BookingStatus> statuses,
                        @Param("minAmount") BigDecimal minAmount);

        // ✅ Query phức tạp 3: Top khách hàng
        @Query("SELECT b.customer.id, COUNT(b), SUM(b.finalAmount) as totalSpent " +
                        "FROM Booking b " +
                        "WHERE b.status = 'CHECKED_OUT' " +
                        "GROUP BY b.customer.id " +
                        "ORDER BY totalSpent DESC")
        List<Object[]> getTopCustomersBySpending();

        // ✅ Query phức tạp 4: Đếm số booking theo trạng thái
        @Query("SELECT b.status, COUNT(b) FROM Booking b GROUP BY b.status")
        List<Object[]> countBookingsByStatus();

        // ✅ Query phức tạp 5: Tìm booking có phòng với loại cụ thể
        @Query("SELECT DISTINCT b FROM Booking b " +
                        "JOIN b.bookingRooms br " +
                        "JOIN br.room r " +
                        "JOIN r.roomType rt " +
                        "WHERE rt.typeName = :typeName " +
                        "AND b.checkInDate >= :fromDate")
        List<Booking> findBookingsByRoomType(
                        @Param("typeName") String typeName,
                        @Param("fromDate") LocalDate fromDate);

        // ✅ Query phức tạp 6: Thống kê booking theo khoảng giá
        @Query("SELECT " +
                        "CASE " +
                        "  WHEN b.finalAmount < 1000000 THEN 'Dưới 1 triệu' " +
                        "  WHEN b.finalAmount < 3000000 THEN '1-3 triệu' " +
                        "  WHEN b.finalAmount < 5000000 THEN '3-5 triệu' " +
                        "  ELSE 'Trên 5 triệu' " +
                        "END as priceRange, " +
                        "COUNT(b) as bookingCount " +
                        "FROM Booking b " +
                        "GROUP BY priceRange " +
                        "ORDER BY MIN(b.finalAmount)")
        List<Object[]> getBookingStatisticsByPriceRange();

        // ✅ Query phức tạp 7: Tính trung bình số đêm và tổng tiền theo tháng
        @Query("SELECT MONTH(b.checkInDate) as month, " +
                        "AVG(b.totalNights) as avgNights, " +
                        "AVG(b.finalAmount) as avgAmount, " +
                        "COUNT(b) as totalBookings " +
                        "FROM Booking b " +
                        "WHERE YEAR(b.checkInDate) = :year " +
                        "GROUP BY MONTH(b.checkInDate) " +
                        "ORDER BY month")
        List<Object[]> getMonthlyStatistics(@Param("year") int year);
}