package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.BookingService;
import com.nguyenhuuquang.hotelmanagement.repository.BookingServiceRepository;
import com.nguyenhuuquang.hotelmanagement.service.BookingServiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceServiceImpl implements BookingServiceService {
    private final BookingServiceRepository bookingServiceRepository;

    @Override
    @Transactional
    public BookingService createBookingService(BookingService bookingService) {
        return bookingServiceRepository.save(bookingService);
    }

    @Override
    @Transactional
    public BookingService updateBookingService(Long id, BookingService bookingService) {
        return bookingServiceRepository.findById(id)
                .map(existing -> {
                    bookingService.setId(id);
                    return bookingServiceRepository.save(bookingService);
                })
                .orElseThrow(() -> new RuntimeException("BookingService not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteBookingService(Long id) {
        bookingServiceRepository.deleteById(id);
    }

    @Override
    public Optional<BookingService> getBookingServiceById(Long id) {
        return bookingServiceRepository.findById(id);
    }

    @Override
    public List<BookingService> getAllBookingServices() {
        return bookingServiceRepository.findAll();
    }

    @Override
    public List<BookingService> getBookingServicesByBookingId(Long bookingId) {
        return bookingServiceRepository.findByBookingId(bookingId);
    }

    @Override
    public List<BookingService> getBookingServicesByServiceId(Long serviceId) {
        return bookingServiceRepository.findByServiceId(serviceId);
    }
}
