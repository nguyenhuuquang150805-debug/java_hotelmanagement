package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.BookingService;
import com.nguyenhuuquang.hotelmanagement.exception.ResourceNotFoundException;
import com.nguyenhuuquang.hotelmanagement.repository.BookingRepository;
import com.nguyenhuuquang.hotelmanagement.repository.BookingServiceRepository;
import com.nguyenhuuquang.hotelmanagement.repository.ServiceRepository;
import com.nguyenhuuquang.hotelmanagement.service.BookingServiceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceServiceImpl implements BookingServiceService {

    private final BookingServiceRepository bookingServiceRepository;
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public BookingService addServiceToBooking(Long bookingId, Long serviceId, Integer quantity) {
        log.info("Adding service {} to booking {} with quantity {}", serviceId, bookingId, quantity);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking với ID: " + bookingId));

        com.nguyenhuuquang.hotelmanagement.entity.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy service với ID: " + serviceId));

        BigDecimal totalPrice = service.getPrice().multiply(BigDecimal.valueOf(quantity));

        BookingService bookingService = BookingService.builder()
                .booking(booking)
                .service(service)
                .quantity(quantity)
                .price(service.getPrice())
                .totalPrice(totalPrice)
                .build();

        bookingService = bookingServiceRepository.save(bookingService);
        log.info("Service added to booking successfully with ID: {}", bookingService.getId());

        return bookingService;
    }

    @Override
    @Transactional
    public void removeServiceFromBooking(Long bookingServiceId) {
        log.info("Removing booking service with ID: {}", bookingServiceId);
        BookingService bookingService = getBookingServiceById(bookingServiceId);
        bookingServiceRepository.delete(bookingService);
        log.info("Booking service removed successfully");
    }

    @Override
    public BookingService getBookingServiceById(Long id) {
        return bookingServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking service với ID: " + id));
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