package com.nguyenhuuquang.hotelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.BookingRoom;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, Long> {
    List<BookingRoom> findByBookingId(Long bookingId);

    List<BookingRoom> findByRoomId(Long roomId);
}