package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.BookingRoom;
import com.nguyenhuuquang.hotelmanagement.repository.BookingRoomRepository;
import com.nguyenhuuquang.hotelmanagement.service.BookingRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingRoomServiceImpl implements BookingRoomService {
    private final BookingRoomRepository bookingRoomRepository;

    @Override
    @Transactional
    public BookingRoom createBookingRoom(BookingRoom bookingRoom) {
        return bookingRoomRepository.save(bookingRoom);
    }

    @Override
    @Transactional
    public BookingRoom updateBookingRoom(Long id, BookingRoom bookingRoom) {
        return bookingRoomRepository.findById(id)
                .map(existing -> {
                    bookingRoom.setId(id);
                    return bookingRoomRepository.save(bookingRoom);
                })
                .orElseThrow(() -> new RuntimeException("BookingRoom not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteBookingRoom(Long id) {
        bookingRoomRepository.deleteById(id);
    }

    @Override
    public Optional<BookingRoom> getBookingRoomById(Long id) {
        return bookingRoomRepository.findById(id);
    }

    @Override
    public List<BookingRoom> getAllBookingRooms() {
        return bookingRoomRepository.findAll();
    }

    @Override
    public List<BookingRoom> getBookingRoomsByBookingId(Long bookingId) {
        return bookingRoomRepository.findByBookingId(bookingId);
    }

    @Override
    public List<BookingRoom> getBookingRoomsByRoomId(Long roomId) {
        return bookingRoomRepository.findByRoomId(roomId);
    }
}