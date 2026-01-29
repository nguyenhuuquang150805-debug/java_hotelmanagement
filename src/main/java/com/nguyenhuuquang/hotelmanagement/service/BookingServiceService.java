package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;

import com.nguyenhuuquang.hotelmanagement.entity.BookingService;

public interface BookingServiceService {

    BookingService addServiceToBooking(Long bookingId, Long serviceId, Integer quantity);

    void removeServiceFromBooking(Long bookingServiceId);

    BookingService getBookingServiceById(Long id);

    List<BookingService> getAllBookingServices();

    List<BookingService> getBookingServicesByBookingId(Long bookingId);

    List<BookingService> getBookingServicesByServiceId(Long serviceId);
}