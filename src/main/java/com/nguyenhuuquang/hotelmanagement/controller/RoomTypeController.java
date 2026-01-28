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

import com.nguyenhuuquang.hotelmanagement.entity.RoomType;
import com.nguyenhuuquang.hotelmanagement.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/room-types")
@RequiredArgsConstructor
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @PostMapping
    public ResponseEntity<EntityModel<RoomType>> createRoomType(@RequestBody RoomType roomType) {
        RoomType createdRoomType = roomTypeService.createRoomType(roomType);
        EntityModel<RoomType> entityModel = toModel(createdRoomType);
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<RoomType>> updateRoomType(@PathVariable Long id, @RequestBody RoomType roomType) {
        RoomType updatedRoomType = roomTypeService.updateRoomType(id, roomType);
        return ResponseEntity.ok(toModel(updatedRoomType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Long id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<RoomType>> getRoomTypeById(@PathVariable Long id) {
        return roomTypeService.getRoomTypeById(id)
                .map(roomType -> ResponseEntity.ok(toModel(roomType)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<EntityModel<RoomType>> getRoomTypeByName(@PathVariable String name) {
        return roomTypeService.getRoomTypeByName(name)
                .map(roomType -> ResponseEntity.ok(toModel(roomType)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<RoomType>>> getAllRoomTypes() {
        List<EntityModel<RoomType>> roomTypes = roomTypeService.getAllRoomTypes().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RoomType>> collectionModel = CollectionModel.of(roomTypes,
                linkTo(methodOn(RoomTypeController.class).getAllRoomTypes()).withSelfRel(),
                linkTo(methodOn(RoomTypeController.class).getActiveRoomTypes()).withRel("active"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/active")
    public ResponseEntity<CollectionModel<EntityModel<RoomType>>> getActiveRoomTypes() {
        List<EntityModel<RoomType>> roomTypes = roomTypeService.getActiveRoomTypes().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<RoomType>> collectionModel = CollectionModel.of(roomTypes,
                linkTo(methodOn(RoomTypeController.class).getActiveRoomTypes()).withSelfRel(),
                linkTo(methodOn(RoomTypeController.class).getAllRoomTypes()).withRel("all"));

        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<RoomType> toModel(RoomType roomType) {
        EntityModel<RoomType> entityModel = EntityModel.of(roomType);

        entityModel.add(linkTo(methodOn(RoomTypeController.class).getRoomTypeById(roomType.getId())).withSelfRel());
        entityModel.add(linkTo(methodOn(RoomTypeController.class).getAllRoomTypes()).withRel("roomTypes"));
        entityModel.add(
                linkTo(methodOn(RoomTypeController.class).updateRoomType(roomType.getId(), null)).withRel("update"));
        entityModel.add(linkTo(methodOn(RoomTypeController.class).deleteRoomType(roomType.getId())).withRel("delete"));
        entityModel.add(linkTo(methodOn(RoomController.class).getRoomsByType(roomType.getId())).withRel("rooms"));

        return entityModel;
    }
}