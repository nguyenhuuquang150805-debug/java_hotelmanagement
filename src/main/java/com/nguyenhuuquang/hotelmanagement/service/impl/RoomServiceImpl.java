package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.dto.request.CreateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.RoomSearchRequest;
import com.nguyenhuuquang.hotelmanagement.dto.request.UpdateRoomRequest;
import com.nguyenhuuquang.hotelmanagement.dto.response.RoomResponse;
import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.RoomType;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;
import com.nguyenhuuquang.hotelmanagement.exception.ResourceNotFoundException;
import com.nguyenhuuquang.hotelmanagement.repository.RoomRepository;
import com.nguyenhuuquang.hotelmanagement.repository.RoomTypeRepository;
import com.nguyenhuuquang.hotelmanagement.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomResponse createRoom(CreateRoomRequest request) {
        log.info("Creating room with number: {}", request.getRoomNumber());

        // Kiểm tra số phòng đã tồn tại
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new IllegalArgumentException("Số phòng đã tồn tại: " + request.getRoomNumber());
        }

        // Lấy room type
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy loại phòng với ID: " + request.getRoomTypeId()));

        // Tạo room
        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .roomType(roomType)
                .floorNumber(request.getFloorNumber())
                .viewDescription(request.getViewDescription())
                .currentPrice(request.getCurrentPrice())
                .status(request.getStatus() != null ? request.getStatus() : RoomStatus.AVAILABLE)
                .isSmoking(request.getIsSmoking() != null ? request.getIsSmoking() : false)
                .hasBalcony(request.getHasBalcony() != null ? request.getHasBalcony() : false)
                .rating(0.0)
                .totalReviews(0)
                .amenities(request.getAmenities())
                .imageUrls(request.getImageUrls())
                .build();

        room = roomRepository.save(room);
        log.info("Room created successfully with ID: {}", room.getId());

        return mapToResponse(room, true);
    }

    @Override
    @Transactional
    public RoomResponse updateRoom(Long id, UpdateRoomRequest request) {
        log.info("Updating room with ID: {}", id);

        Room room = findRoomEntityById(id);

        // Cập nhật các trường nếu có trong request
        if (request.getRoomNumber() != null && !request.getRoomNumber().equals(room.getRoomNumber())) {
            if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
                throw new IllegalArgumentException("Số phòng đã tồn tại: " + request.getRoomNumber());
            }
            room.setRoomNumber(request.getRoomNumber());
        }

        if (request.getRoomTypeId() != null) {
            RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy loại phòng với ID: " + request.getRoomTypeId()));
            room.setRoomType(roomType);
        }

        if (request.getFloorNumber() != null) {
            room.setFloorNumber(request.getFloorNumber());
        }

        if (request.getViewDescription() != null) {
            room.setViewDescription(request.getViewDescription());
        }

        if (request.getCurrentPrice() != null) {
            room.setCurrentPrice(request.getCurrentPrice());
        }

        if (request.getStatus() != null) {
            room.setStatus(request.getStatus());
        }

        if (request.getIsSmoking() != null) {
            room.setIsSmoking(request.getIsSmoking());
        }

        if (request.getHasBalcony() != null) {
            room.setHasBalcony(request.getHasBalcony());
        }

        if (request.getAmenities() != null) {
            room.setAmenities(request.getAmenities());
        }

        if (request.getImageUrls() != null) {
            room.setImageUrls(request.getImageUrls());
        }

        room = roomRepository.save(room);
        log.info("Room updated successfully with ID: {}", room.getId());

        return mapToResponse(room, true);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id) {
        log.info("Deleting room with ID: {}", id);
        Room room = findRoomEntityById(id);
        roomRepository.delete(room);
        log.info("Room deleted successfully with ID: {}", id);
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        Room room = findRoomEntityById(id);
        return mapToResponse(room, true);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> mapToResponse(room, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status).stream()
                .map(room -> mapToResponse(room, true))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> searchAvailableRooms(RoomSearchRequest request) {
        log.info("Searching available rooms from {} to {}",
                request.getCheckInDate(), request.getCheckOutDate());

        // Validate dates
        if (request.getCheckOutDate().isBefore(request.getCheckInDate())) {
            throw new IllegalArgumentException("Ngày check-out phải sau ngày check-in");
        }

        if (request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày check-in phải từ hôm nay trở đi");
        }

        List<Room> availableRooms = roomRepository.searchAvailableRooms(
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getRoomTypeId(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getIsSmoking(),
                request.getHasBalcony(),
                request.getFloorNumber());

        log.info("Found {} available rooms", availableRooms.size());

        return availableRooms.stream()
                .map(room -> mapToResponse(room, true))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        log.info("Checking availability for room {} from {} to {}",
                roomId, checkInDate, checkOutDate);

        Room room = findRoomEntityById(roomId);

        // Kiểm tra status phòng
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            log.info("Room {} is not available - status: {}", roomId, room.getStatus());
            return false;
        }

        // Kiểm tra booking conflicts
        boolean available = roomRepository.isRoomAvailable(roomId, checkInDate, checkOutDate);
        log.info("Room {} availability check result: {}", roomId, available);

        return available;
    }

    @Override
    @Transactional
    public RoomResponse updateRoomStatus(Long id, RoomStatus status) {
        log.info("Updating room {} status to {}", id, status);
        Room room = findRoomEntityById(id);
        room.setStatus(status);
        room = roomRepository.save(room);
        return mapToResponse(room, true);
    }

    @Override
    public Room findRoomEntityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng với ID: " + id));
    }

    private RoomResponse mapToResponse(Room room, Boolean isAvailable) {
        RoomResponse.RoomTypeInfo roomTypeInfo = RoomResponse.RoomTypeInfo.builder()
                .id(room.getRoomType().getId())
                .name(room.getRoomType().getName())
                .capacity(room.getRoomType().getCapacity())
                .description(room.getRoomType().getDescription())
                .basePrice(room.getRoomType().getBasePrice())
                .build();

        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .roomType(roomTypeInfo)
                .floorNumber(room.getFloorNumber())
                .viewDescription(room.getViewDescription())
                .currentPrice(room.getCurrentPrice())
                .status(room.getStatus())
                .isSmoking(room.getIsSmoking())
                .hasBalcony(room.getHasBalcony())
                .rating(room.getRating())
                .totalReviews(room.getTotalReviews())
                .amenities(room.getAmenities())
                .imageUrls(room.getImageUrls())
                .isAvailable(isAvailable)
                .build();
    }
}