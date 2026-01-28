package com.nguyenhuuquang.hotelmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.BookingService;
import com.nguyenhuuquang.hotelmanagement.service.BookingServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/booking-services")
@RequiredArgsConstructor
public class BookingServiceController {
    private final BookingServiceService bookingServiceService;

    @PostMapping
    public ResponseEntity<BookingService> createBookingService(@RequestBody BookingService bookingService) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingServiceService.createBookingService(bookingService));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingService> updateBookingService(@PathVariable Long id,
            @RequestBody BookingService bookingService) {
        return ResponseEntity.ok(bookingServiceService.updateBookingService(id, bookingService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingService(@PathVariable Long id) {
        bookingServiceService.deleteBookingService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingService> getBookingServiceById(@PathVariable Long id) {
        return bookingServiceService.getBookingServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BookingService>> getAllBookingServices() {
        return ResponseEntity.ok(bookingServiceService.getAllBookingServices());
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingService>> getBookingServicesByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingServiceService.getBookingServicesByBookingId(bookingId));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<BookingService>> getBookingServicesByService(@PathVariable Long serviceId) {
        return ResponseEntity.ok(bookingServiceService.getBookingServicesByServiceId(serviceId));
    }
}