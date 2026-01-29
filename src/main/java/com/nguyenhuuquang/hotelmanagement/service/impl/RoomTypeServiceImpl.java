package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.RoomType;
import com.nguyenhuuquang.hotelmanagement.exception.ResourceNotFoundException;
import com.nguyenhuuquang.hotelmanagement.repository.RoomTypeRepository;
import com.nguyenhuuquang.hotelmanagement.service.RoomTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomType createRoomType(RoomType roomType) {
        log.info("Creating room type: {}", roomType.getName());

        if (roomTypeRepository.existsByName(roomType.getName())) {
            throw new IllegalArgumentException("Room type with name '" + roomType.getName() + "' already exists");
        }

        if (roomType.getIsActive() == null) {
            roomType.setIsActive(true);
        }

        RoomType saved = roomTypeRepository.save(roomType);
        log.info("Room type created with ID: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional
    public RoomType updateRoomType(Long id, RoomType roomType) {
        log.info("Updating room type with ID: {}", id);

        RoomType existing = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room type not found with ID: " + id));

        // Check name uniqueness if changed
        if (!existing.getName().equals(roomType.getName()) &&
                roomTypeRepository.existsByName(roomType.getName())) {
            throw new IllegalArgumentException("Room type with name '" + roomType.getName() + "' already exists");
        }

        existing.setName(roomType.getName());
        existing.setCapacity(roomType.getCapacity());
        existing.setDescription(roomType.getDescription());
        existing.setBasePrice(roomType.getBasePrice());

        if (roomType.getIsActive() != null) {
            existing.setIsActive(roomType.getIsActive());
        }

        RoomType updated = roomTypeRepository.save(existing);
        log.info("Room type updated successfully");
        return updated;
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {
        log.info("Deleting room type with ID: {}", id);

        if (!roomTypeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room type not found with ID: " + id);
        }

        roomTypeRepository.deleteById(id);
        log.info("Room type deleted successfully");
    }

    @Override
    public Optional<RoomType> getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public Optional<RoomType> getRoomTypeByName(String name) {
        return roomTypeRepository.findByName(name);
    }

    @Override
    public List<RoomType> getAllRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Override
    public List<RoomType> getActiveRoomTypes() {
        return roomTypeRepository.findByIsActive(true);
    }
}