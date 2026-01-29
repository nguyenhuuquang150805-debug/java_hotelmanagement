package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;

import com.nguyenhuuquang.hotelmanagement.dto.request.CreateBookingRequest;
import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;

public interface BookingService {

    Booking createBooking(CreateBookingRequest request, Long userId);

    Booking getBookingById(Long id);

    List<Booking> getUserBookings(Long userId);

    List<Booking> getRoomBookings(Long roomId);

    Booking updateBookingStatus(Long id, BookingStatus status);

    void cancelBooking(Long id, String reason);

    Booking checkIn(Long id);

    Booking checkOut(Long id);
}