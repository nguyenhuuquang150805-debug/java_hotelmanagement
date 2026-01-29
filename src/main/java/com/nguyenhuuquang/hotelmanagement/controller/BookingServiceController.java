package com.nguyenhuuquang.hotelmanagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingService> addServiceToBooking(
            @RequestParam Long bookingId,
            @RequestParam Long serviceId,
            @RequestParam Integer quantity) {
        BookingService bookingService = bookingServiceService.addServiceToBooking(bookingId, serviceId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeServiceFromBooking(@PathVariable Long id) {
        bookingServiceService.removeServiceFromBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingService> getBookingServiceById(@PathVariable Long id) {
        BookingService bookingService = bookingServiceService.getBookingServiceById(id);
        return ResponseEntity.ok(bookingService);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingService>> getAllBookingServices() {
        List<BookingService> bookingServices = bookingServiceService.getAllBookingServices();
        return ResponseEntity.ok(bookingServices);
    }

    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingService>> getBookingServicesByBookingId(@PathVariable Long bookingId) {
        List<BookingService> bookingServices = bookingServiceService.getBookingServicesByBookingId(bookingId);
        return ResponseEntity.ok(bookingServices);
    }

    @GetMapping("/service/{serviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingService>> getBookingServicesByServiceId(@PathVariable Long serviceId) {
        List<BookingService> bookingServices = bookingServiceService.getBookingServicesByServiceId(serviceId);
        return ResponseEntity.ok(bookingServices);
    }
}