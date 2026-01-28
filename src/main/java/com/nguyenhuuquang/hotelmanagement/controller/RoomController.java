package com.nguyenhuuquang.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;
import com.nguyenhuuquang.hotelmanagement.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
        private final RoomService roomService;

        @PostMapping
        public ResponseEntity<EntityModel<Room>> createRoom(@RequestBody Room room) {
                Room createdRoom = roomService.createRoom(room);
                EntityModel<Room> entityModel = toModel(createdRoom);
                return ResponseEntity
                                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                                .body(entityModel);
        }

        @PutMapping("/{id}")
        public ResponseEntity<EntityModel<Room>> updateRoom(@PathVariable Long id, @RequestBody Room room) {
                Room updatedRoom = roomService.updateRoom(id, room);
                return ResponseEntity.ok(toModel(updatedRoom));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
                roomService.deleteRoom(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<EntityModel<Room>> getRoomById(@PathVariable Long id) {
                return roomService.getRoomById(id)
                                .map(room -> ResponseEntity.ok(toModel(room)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/number/{number}")
        public ResponseEntity<EntityModel<Room>> getRoomByNumber(@PathVariable String number) {
                return roomService.getRoomByNumber(number)
                                .map(room -> ResponseEntity.ok(toModel(room)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getAllRooms() {
                List<EntityModel<Room>> rooms = roomService.getAllRooms().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getAllRooms()).withSelfRel());

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/type/{typeId}")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getRoomsByType(@PathVariable Long typeId) {
                List<EntityModel<Room>> rooms = roomService.getRoomsByTypeId(typeId).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getRoomsByType(typeId)).withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/status/{status}")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getRoomsByStatus(@PathVariable RoomStatus status) {
                List<EntityModel<Room>> rooms = roomService.getRoomsByStatus(status).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getRoomsByStatus(status)).withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/floor/{floor}")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getRoomsByFloor(@PathVariable Integer floor) {
                List<EntityModel<Room>> rooms = roomService.getRoomsByFloor(floor).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getRoomsByFloor(floor)).withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<EntityModel<Room>> updateRoomStatus(
                        @PathVariable Long id,
                        @RequestParam RoomStatus status) {
                Room updatedRoom = roomService.updateRoomStatus(id, status);
                return ResponseEntity.ok(toModel(updatedRoom));
        }

        // Các endpoint mới cho query phức tạp

        @GetMapping("/available")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getAvailableRooms(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

                List<EntityModel<Room>> rooms = roomService.findAvailableRooms(checkIn, checkOut).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getAvailableRooms(checkIn, checkOut))
                                                .withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/statistics")
        public ResponseEntity<List<Object[]>> getRoomStatistics() {
                return ResponseEntity.ok(roomService.getRoomStatistics());
        }

        @GetMapping("/search")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> searchRooms(
                        @RequestParam(required = false) Long typeId,
                        @RequestParam(required = false) Integer floor,
                        @RequestParam(required = false) Double minRating) {

                List<EntityModel<Room>> rooms = roomService.searchRooms(typeId, floor, minRating).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).searchRooms(typeId, floor, minRating))
                                                .withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/top-booked")
        public ResponseEntity<List<Object[]>> getTopBookedRooms() {
                return ResponseEntity.ok(roomService.getTopBookedRooms());
        }

        @GetMapping("/price-range")
        public ResponseEntity<List<Object[]>> getRoomsByPriceRange() {
                return ResponseEntity.ok(roomService.getRoomsByPriceRange());
        }

        @GetMapping("/rating")
        public ResponseEntity<List<Object[]>> getRoomTypesByRating(
                        @RequestParam(defaultValue = "3.0") Double minAvgRating) {
                return ResponseEntity.ok(roomService.getRoomTypesByRating(minAvgRating));
        }

        @GetMapping("/unbooked")
        public ResponseEntity<CollectionModel<EntityModel<Room>>> getUnbookedRoomsInPeriod(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

                List<EntityModel<Room>> rooms = roomService.findUnbookedRoomsInPeriod(startDate, endDate).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Room>> collectionModel = CollectionModel.of(rooms,
                                linkTo(methodOn(RoomController.class).getUnbookedRoomsInPeriod(startDate, endDate))
                                                .withSelfRel(),
                                linkTo(methodOn(RoomController.class).getAllRooms()).withRel("all-rooms"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/floor-statistics")
        public ResponseEntity<List<Object[]>> getFloorStatistics() {
                return ResponseEntity.ok(roomService.getFloorStatistics());
        }

        private EntityModel<Room> toModel(Room room) {
                EntityModel<Room> entityModel = EntityModel.of(room);

                entityModel.add(linkTo(methodOn(RoomController.class).getRoomById(room.getId())).withSelfRel());
                entityModel.add(linkTo(methodOn(RoomController.class).getAllRooms()).withRel("rooms"));
                entityModel.add(linkTo(methodOn(RoomController.class).updateRoom(room.getId(), null))
                                .withRel("update"));
                entityModel.add(linkTo(methodOn(RoomController.class).deleteRoom(room.getId())).withRel("delete"));

                if (room.getRoomType() != null) {
                        entityModel.add(linkTo(methodOn(RoomTypeController.class)
                                        .getRoomTypeById(room.getRoomType().getId())).withRel("roomType"));
                }

                entityModel.add(linkTo(methodOn(RoomController.class)
                                .updateRoomStatus(room.getId(), null)).withRel("updateStatus"));

                return entityModel;
        }
}