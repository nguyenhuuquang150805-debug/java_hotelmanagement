package com.nguyenhuuquang.hotelmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;

public interface RoomService {
    Room createRoom(Room room);

    Room updateRoom(Long id, Room room);

    void deleteRoom(Long id);

    Optional<Room> getRoomById(Long id);

    Optional<Room> getRoomByNumber(String roomNumber);

    List<Room> getAllRooms();

    List<Room> getRoomsByTypeId(Long roomTypeId);

    List<Room> getRoomsByStatus(RoomStatus status);

    List<Room> getRoomsByFloor(Integer floorNumber);

    Room updateRoomStatus(Long id, RoomStatus status);

    // truy vấn phức tạp
    List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut);

    List<Object[]> getRoomStatistics();

    List<Room> searchRooms(Long typeId, Integer floor, Double minRating);

    List<Object[]> getTopBookedRooms();

    List<Object[]> getRoomsByPriceRange();

    List<Object[]> getRoomTypesByRating(Double minAvgRating);

    List<Room> findUnbookedRoomsInPeriod(LocalDate startDate, LocalDate endDate);

    List<Object[]> getFloorStatistics();

}