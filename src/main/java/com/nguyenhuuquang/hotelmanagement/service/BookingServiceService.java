package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.BookingService;

public interface BookingServiceService {
    BookingService createBookingService(BookingService bookingService);

    BookingService updateBookingService(Long id, BookingService bookingService);

    void deleteBookingService(Long id);

    Optional<BookingService> getBookingServiceById(Long id);

    List<BookingService> getAllBookingServices();

    List<BookingService> getBookingServicesByBookingId(Long bookingId);

    List<BookingService> getBookingServicesByServiceId(Long serviceId);
}