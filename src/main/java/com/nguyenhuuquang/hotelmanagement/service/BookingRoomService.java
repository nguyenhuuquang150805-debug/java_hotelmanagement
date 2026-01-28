package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.BookingRoom;

public interface BookingRoomService {
    BookingRoom createBookingRoom(BookingRoom bookingRoom);

    BookingRoom updateBookingRoom(Long id, BookingRoom bookingRoom);

    void deleteBookingRoom(Long id);

    Optional<BookingRoom> getBookingRoomById(Long id);

    List<BookingRoom> getAllBookingRooms();

    List<BookingRoom> getBookingRoomsByBookingId(Long bookingId);

    List<BookingRoom> getBookingRoomsByRoomId(Long roomId);
}