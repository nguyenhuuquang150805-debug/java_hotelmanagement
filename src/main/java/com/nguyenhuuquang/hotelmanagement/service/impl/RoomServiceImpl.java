package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;
import com.nguyenhuuquang.hotelmanagement.repository.RoomRepository;
import com.nguyenhuuquang.hotelmanagement.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Override
    @Transactional
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room updateRoom(Long id, Room room) {
        return roomRepository.findById(id)
                .map(existing -> {
                    room.setId(id);
                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Override
    public Optional<Room> getRoomByNumber(String roomNumber) {
        return roomRepository.findByRoomNumber(roomNumber);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByTypeId(Long roomTypeId) {
        return roomRepository.findByRoomTypeId(roomTypeId);
    }

    @Override
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status);
    }

    @Override
    public List<Room> getRoomsByFloor(Integer floorNumber) {
        return roomRepository.findByFloorNumber(floorNumber);
    }

    @Override
    @Transactional
    public Room updateRoomStatus(Long id, RoomStatus status) {
        return roomRepository.findById(id)
                .map(room -> {
                    room.setStatus(status);
                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
    }

    // query phức tạp
    @Override
    public List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(checkIn, checkOut);
    }

    @Override
    public List<Object[]> getRoomStatistics() {
        return roomRepository.getRoomStatistics();
    }

    @Override
    public List<Room> searchRooms(Long typeId, Integer floor, Double minRating) {
        return roomRepository.searchRooms(typeId, floor, minRating);
    }

    @Override
    public List<Object[]> getTopBookedRooms() {
        return roomRepository.getTopBookedRooms();
    }

    @Override
    public List<Object[]> getRoomsByPriceRange() {
        return roomRepository.getRoomsByPriceRange();
    }

    @Override
    public List<Object[]> getRoomTypesByRating(Double minAvgRating) {
        return roomRepository.getRoomTypesByRating(minAvgRating);
    }

    @Override
    public List<Room> findUnbookedRoomsInPeriod(LocalDate startDate, LocalDate endDate) {
        return roomRepository.findUnbookedRoomsInPeriod(startDate, endDate);
    }

    @Override
    public List<Object[]> getFloorStatistics() {
        return roomRepository.getFloorStatistics();
    }
}