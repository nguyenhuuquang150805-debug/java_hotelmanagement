package com.nguyenhuuquang.hotelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.BookingService;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingService, Long> {
    List<BookingService> findByBookingId(Long bookingId);

    List<BookingService> findByServiceId(Long serviceId);
}