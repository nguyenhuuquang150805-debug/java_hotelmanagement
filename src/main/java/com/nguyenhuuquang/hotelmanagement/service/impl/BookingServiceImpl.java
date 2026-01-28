package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;
import com.nguyenhuuquang.hotelmanagement.repository.BookingRepository;
import com.nguyenhuuquang.hotelmanagement.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking createBookingWithValidation(Booking booking) {
        if (booking.getCheckInDate().isAfter(booking.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (booking.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        if (booking.getBookingRooms() != null && !booking.getBookingRooms().isEmpty()) {
            List<Long> requestedRoomIds = booking.getBookingRooms().stream()
                    .map(br -> br.getRoom().getId())
                    .collect(Collectors.toList());

            List<Long> occupiedRoomIds = getOccupiedRoomIds(
                    requestedRoomIds,
                    booking.getCheckInDate(),
                    booking.getCheckOutDate());

            if (!occupiedRoomIds.isEmpty()) {
                String occupiedRooms = occupiedRoomIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));
                throw new IllegalStateException(
                        "Rooms are not available for the selected dates. Occupied room IDs: " + occupiedRooms);
            }
        }

        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Booking updateBooking(Long id, Booking booking) {
        return bookingRepository.findById(id)
                .map(existing -> {
                    booking.setId(id);
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Optional<Booking> getBookingByCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end) {
        return bookingRepository.findByCheckInDateBetween(start, end);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        return bookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(status);
                    return bookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public boolean checkRoomAvailability(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomId, checkIn, checkOut);
        return overlappingBookings.isEmpty();
    }

    @Override
    public List<Long> getOccupiedRoomIds(List<Long> roomIds, LocalDate checkIn, LocalDate checkOut) {
        return bookingRepository.findOccupiedRoomIds(roomIds, checkIn, checkOut);
    }
}