package com.nguyenhuuquang.hotelmanagement.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.BookingRoom;
import com.nguyenhuuquang.hotelmanagement.service.BookingRoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/booking-rooms")
@RequiredArgsConstructor
public class BookingRoomController {
        private final BookingRoomService bookingRoomService;

        @PostMapping
        public ResponseEntity<EntityModel<BookingRoom>> createBookingRoom(@RequestBody BookingRoom bookingRoom) {
                BookingRoom createdBookingRoom = bookingRoomService.createBookingRoom(bookingRoom);
                EntityModel<BookingRoom> entityModel = toModel(createdBookingRoom);
                return ResponseEntity
                                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                                .body(entityModel);
        }

        @PutMapping("/{id}")
        public ResponseEntity<EntityModel<BookingRoom>> updateBookingRoom(@PathVariable Long id,
                        @RequestBody BookingRoom bookingRoom) {
                BookingRoom updatedBookingRoom = bookingRoomService.updateBookingRoom(id, bookingRoom);
                return ResponseEntity.ok(toModel(updatedBookingRoom));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteBookingRoom(@PathVariable Long id) {
                bookingRoomService.deleteBookingRoom(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<EntityModel<BookingRoom>> getBookingRoomById(@PathVariable Long id) {
                return bookingRoomService.getBookingRoomById(id)
                                .map(bookingRoom -> ResponseEntity.ok(toModel(bookingRoom)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<CollectionModel<EntityModel<BookingRoom>>> getAllBookingRooms() {
                List<EntityModel<BookingRoom>> bookingRooms = bookingRoomService.getAllBookingRooms().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<BookingRoom>> collectionModel = CollectionModel.of(bookingRooms,
                                linkTo(methodOn(BookingRoomController.class).getAllBookingRooms()).withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/booking/{bookingId}")
        public ResponseEntity<CollectionModel<EntityModel<BookingRoom>>> getBookingRoomsByBooking(
                        @PathVariable Long bookingId) {
                List<EntityModel<BookingRoom>> bookingRooms = bookingRoomService.getBookingRoomsByBookingId(bookingId)
                                .stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<BookingRoom>> collectionModel = CollectionModel.of(bookingRooms,
                                linkTo(methodOn(BookingRoomController.class).getBookingRoomsByBooking(bookingId))
                                                .withSelfRel(),
                                linkTo(methodOn(BookingRoomController.class).getAllBookingRooms())
                                                .withRel("all-booking-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/room/{roomId}")
        public ResponseEntity<CollectionModel<EntityModel<BookingRoom>>> getBookingRoomsByRoom(
                        @PathVariable Long roomId) {
                List<EntityModel<BookingRoom>> bookingRooms = bookingRoomService.getBookingRoomsByRoomId(roomId)
                                .stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<BookingRoom>> collectionModel = CollectionModel.of(bookingRooms,
                                linkTo(methodOn(BookingRoomController.class).getBookingRoomsByRoom(roomId))
                                                .withSelfRel(),
                                linkTo(methodOn(BookingRoomController.class).getAllBookingRooms())
                                                .withRel("all-booking-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        private EntityModel<BookingRoom> toModel(BookingRoom bookingRoom) {
                EntityModel<BookingRoom> entityModel = EntityModel.of(bookingRoom);

                entityModel.add(
                                linkTo(methodOn(BookingRoomController.class).getBookingRoomById(bookingRoom.getId()))
                                                .withSelfRel());
                entityModel.add(linkTo(methodOn(BookingRoomController.class).getAllBookingRooms())
                                .withRel("bookingRooms"));
                entityModel.add(linkTo(
                                methodOn(BookingRoomController.class).updateBookingRoom(bookingRoom.getId(), null))
                                .withRel("update"));
                entityModel.add(
                                linkTo(methodOn(BookingRoomController.class).deleteBookingRoom(bookingRoom.getId()))
                                                .withRel("delete"));

                if (bookingRoom.getBooking() != null) {
                        entityModel.add(linkTo(methodOn(BookingController.class)
                                        .getBookingById(bookingRoom.getBooking().getId())).withRel("booking"));
                }

                if (bookingRoom.getRoom() != null) {
                        entityModel.add(linkTo(methodOn(RoomController.class)
                                        .getRoomById(bookingRoom.getRoom().getId())).withRel("room"));
                }

                return entityModel;
        }
}