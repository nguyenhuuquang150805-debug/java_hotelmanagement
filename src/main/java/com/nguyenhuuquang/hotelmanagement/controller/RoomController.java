package com.nguyenhuuquang.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.nguyenhuuquang.hotelmanagement.dto.request.CreateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.RoomSearchRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.UpdateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.response.RoomResponse;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;
import com.nguyenhuuquang.hotelmanagement.service.RoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

        private final RoomService roomService;

        @GetMapping("/search")
        public ResponseEntity<List<RoomResponse>> searchAvailableRooms(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                        @RequestParam(required = false) Long roomTypeId,
                        @RequestParam(required = false) Integer numberOfGuests,
                        @RequestParam(required = false) java.math.BigDecimal minPrice,
                        @RequestParam(required = false) java.math.BigDecimal maxPrice,
                        @RequestParam(required = false) Boolean isSmoking,
                        @RequestParam(required = false) Boolean hasBalcony,
                        @RequestParam(required = false) Integer floorNumber) {

                RoomSearchRequest request = RoomSearchRequest.builder()
                                .checkInDate(checkInDate)
                                .checkOutDate(checkOutDate)
                                .roomTypeId(roomTypeId)
                                .numberOfGuests(numberOfGuests)
                                .minPrice(minPrice)
                                .maxPrice(maxPrice)
                                .isSmoking(isSmoking)
                                .hasBalcony(hasBalcony)
                                .floorNumber(floorNumber)
                                .build();

                List<RoomResponse> rooms = roomService.searchAvailableRooms(request);
                return ResponseEntity.ok(rooms);
        }

        @GetMapping("/{id}/availability")
        public ResponseEntity<Boolean> checkRoomAvailability(
                        @PathVariable Long id,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

                boolean available = roomService.isRoomAvailable(id, checkInDate, checkOutDate);
                return ResponseEntity.ok(available);
        }

        @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
                RoomResponse room = roomService.createRoom(request);
                return ResponseEntity.status(HttpStatus.CREATED).body(room);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<RoomResponse> updateRoom(
                        @PathVariable Long id,
                        @Valid @RequestBody UpdateRoomRequest request) {
                RoomResponse room = roomService.updateRoom(id, request);
                return ResponseEntity.ok(room);
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
                roomService.deleteRoom(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
                RoomResponse room = roomService.getRoomById(id);
                return ResponseEntity.ok(room);
        }

        @GetMapping
        public ResponseEntity<List<RoomResponse>> getAllRooms() {
                List<RoomResponse> rooms = roomService.getAllRooms();
                return ResponseEntity.ok(rooms);
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<List<RoomResponse>> getRoomsByStatus(@PathVariable RoomStatus status) {
                List<RoomResponse> rooms = roomService.getRoomsByStatus(status);
                return ResponseEntity.ok(rooms);
        }

        @PatchMapping("/{id}/status")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<RoomResponse> updateRoomStatus(
                        @PathVariable Long id,
                        @RequestParam RoomStatus status) {
                RoomResponse room = roomService.updateRoomStatus(id, status);
                return ResponseEntity.ok(room);
        }
}