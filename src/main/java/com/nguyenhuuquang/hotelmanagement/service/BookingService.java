package com.nguyenhuuquang.hotelmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;

public interface BookingService {
    Booking createBooking(Booking booking);

    Booking createBookingWithValidation(Booking booking);

    Booking updateBooking(Long id, Booking booking);

    void deleteBooking(Long id);

    Optional<Booking> getBookingById(Long id);

    Optional<Booking> getBookingByCode(String bookingCode);

    List<Booking> getAllBookings();

    List<Booking> getBookingsByCustomerId(Long customerId);

    List<Booking> getBookingsByStatus(BookingStatus status);

    List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end);

    Booking updateBookingStatus(Long id, BookingStatus status);

    boolean checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<Long> getOccupiedRoomIds(List<Long> roomIds, LocalDate checkIn, LocalDate checkOut);
}