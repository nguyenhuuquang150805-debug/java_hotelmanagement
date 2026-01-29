package com.nguyenhuuquang.hotelmanagement.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

        List<Booking> findByUserId(Long userId);

        List<Booking> findByRoomId(Long roomId);

        List<Booking> findByStatus(BookingStatus status);

        @Query("SELECT b FROM Booking b WHERE b.user.id = :userId AND b.status = :status")
        List<Booking> findByUserIdAndStatus(
                        @Param("userId") Long userId,
                        @Param("status") BookingStatus status);

        @Query("SELECT b FROM Booking b " +
                        "WHERE b.room.id = :roomId " +
                        "AND b.status IN ('CONFIRMED', 'CHECKED_IN', 'PENDING') " +
                        "AND (" +
                        "  (b.checkInDate <= :checkOutDate AND b.checkOutDate >= :checkInDate)" +
                        ")")
        List<Booking> findConflictingBookings(
                        @Param("roomId") Long roomId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);
}