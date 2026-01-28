package com.nguyenhuuquang.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;
import com.nguyenhuuquang.hotelmanagement.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    private EntityModel<Booking> toModel(Booking booking) {
        return EntityModel.of(booking,
                linkTo(methodOn(BookingController.class).getBookingById(booking.getId())).withSelfRel(),
                linkTo(methodOn(BookingController.class).getAllBookings()).withRel("all-bookings"),
                linkTo(methodOn(BookingController.class).updateBookingStatus(booking.getId(), null))
                        .withRel("update-status"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Booking>> createBooking(@RequestBody Booking booking) {
        Booking created = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(created));
    }

    @PostMapping("/validated")
    public ResponseEntity<EntityModel<Booking>> createBookingWithValidation(@RequestBody Booking booking) {
        try {
            Booking created = bookingService.createBookingWithValidation(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(toModel(created));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Booking>> getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id)
                .map(this::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Booking>>> getAllBookings() {
        List<EntityModel<Booking>> bookings = bookingService.getAllBookings().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(bookings,
                linkTo(methodOn(BookingController.class).getAllBookings()).withSelfRel()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EntityModel<Booking>> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        Booking updated = bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(toModel(updated));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.updateBooking(id, booking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<Booking> getBookingByCode(@PathVariable String code) {
        return bookingService.getBookingByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getBookingsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable BookingStatus status) {
        return ResponseEntity.ok(bookingService.getBookingsByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Booking>> getBookingsByDateRange(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(bookingService.getBookingsByDateRange(start, end));
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        boolean available = bookingService.checkRoomAvailability(roomId, checkIn, checkOut);
        return ResponseEntity.ok(available);
    }

    @PostMapping("/check-rooms-availability")
    public ResponseEntity<List<Long>> checkMultipleRoomsAvailability(
            @RequestParam List<Long> roomIds,
            @RequestParam LocalDate checkIn,
            @RequestParam LocalDate checkOut) {
        List<Long> occupiedRooms = bookingService.getOccupiedRoomIds(roomIds, checkIn, checkOut);
        return ResponseEntity.ok(occupiedRooms);
    }
}