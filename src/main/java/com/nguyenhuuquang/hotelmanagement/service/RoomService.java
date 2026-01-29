package com.nguyenhuuquang.hotelmanagement.service;

import java.time.LocalDate;
import java.util.List;

import com.nguyenhuuquang.hotelmanagement.dto.request.CreateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.RoomSearchRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.UpdateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.response.RoomResponse;
import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;

public interface RoomService {

    RoomResponse createRoom(CreateRoomRequest request);

    RoomResponse updateRoom(Long id, UpdateRoomRequest request);

    void deleteRoom(Long id);

    RoomResponse getRoomById(Long id);

    List<RoomResponse> getAllRooms();

    List<RoomResponse> getRoomsByStatus(RoomStatus status);

    List<RoomResponse> searchAvailableRooms(RoomSearchRequest request);

    boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate);

    RoomResponse updateRoomStatus(Long id, RoomStatus status);

    Room findRoomEntityById(Long id);
}