package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.RoomType;

public interface RoomTypeService {
    RoomType createRoomType(RoomType roomType);

    RoomType updateRoomType(Long id, RoomType roomType);

    void deleteRoomType(Long id);

    Optional<RoomType> getRoomTypeById(Long id);

    Optional<RoomType> getRoomTypeByName(String typeName);

    List<RoomType> getAllRoomTypes();

    List<RoomType> getActiveRoomTypes();
}
