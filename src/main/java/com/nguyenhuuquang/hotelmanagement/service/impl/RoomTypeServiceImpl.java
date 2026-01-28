package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.RoomType;
import com.nguyenhuuquang.hotelmanagement.repository.RoomTypeRepository;
import com.nguyenhuuquang.hotelmanagement.service.RoomTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional
    public RoomType createRoomType(RoomType roomType) {
        return roomTypeRepository.save(roomType);
    }

    @Override
    @Transactional
    public RoomType updateRoomType(Long id, RoomType roomType) {
        return roomTypeRepository.findById(id)
                .map(existing -> {
                    roomType.setId(id);
                    return roomTypeRepository.save(roomType);
                })
                .orElseThrow(() -> new RuntimeException("RoomType not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteRoomType(Long id) {
        roomTypeRepository.deleteById(id);
    }

    @Override
    public Optional<RoomType> getRoomTypeById(Long id) {
        return roomTypeRepository.findById(id);
    }

    @Override
    public Optional<RoomType> getRoomTypeByName(String typeName) {
        return roomTypeRepository.findByTypeName(typeName);
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