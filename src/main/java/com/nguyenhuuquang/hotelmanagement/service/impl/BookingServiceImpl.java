package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.dto.request.CreateBookingRequest;
import com.nguyenhuuquang.hotelmanagement.entity.Booking;
import com.nguyenhuuquang.hotelmanagement.entity.Room;
import com.nguyenhuuquang.hotelmanagement.entity.User;
import com.nguyenhuuquang.hotelmanagement.entity.enums.BookingStatus;
import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;
import com.nguyenhuuquang.hotelmanagement.exception.BookingException;
import com.nguyenhuuquang.hotelmanagement.exception.ResourceNotFoundException;
import com.nguyenhuuquang.hotelmanagement.repository.BookingRepository;
import com.nguyenhuuquang.hotelmanagement.repository.UserRepository;
import com.nguyenhuuquang.hotelmanagement.service.BookingService;
import com.nguyenhuuquang.hotelmanagement.service.RoomService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomService roomService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Booking createBooking(CreateBookingRequest request, Long userId) {
        log.info("Creating booking for user {} and room {}", userId, request.getRoomId());

        // Validate dates
        if (request.getCheckOutDate().isBefore(request.getCheckInDate()) ||
                request.getCheckOutDate().isEqual(request.getCheckInDate())) {
            throw new BookingException("Ngày check-out phải sau ngày check-in ít nhất 1 ngày");
        }

        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user với ID: " + userId));

        // Lấy room
        Room room = roomService.findRoomEntityById(request.getRoomId());

        // Kiểm tra room status
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BookingException("Phòng này không khả dụng. Trạng thái: " + room.getStatus());
        }

        // Kiểm tra capacity
        if (request.getNumberOfGuests() > room.getRoomType().getCapacity()) {
            throw new BookingException(String.format(
                    "Số lượng khách (%d) vượt quá sức chứa của phòng (%d)",
                    request.getNumberOfGuests(),
                    room.getRoomType().getCapacity()));
        }

        // Kiểm tra xem phòng có available không (quan trọng nhất)
        boolean isAvailable = roomService.isRoomAvailable(
                request.getRoomId(),
                request.getCheckInDate(),
                request.getCheckOutDate());

        if (!isAvailable) {
            throw new BookingException(
                    "Phòng đã được đặt trong khoảng thời gian từ " +
                            request.getCheckInDate() + " đến " + request.getCheckOutDate());
        }

        // Tính tổng giá
        long numberOfNights = ChronoUnit.DAYS.between(
                request.getCheckInDate(),
                request.getCheckOutDate());
        BigDecimal totalPrice = room.getCurrentPrice()
                .multiply(BigDecimal.valueOf(numberOfNights));

        // Tạo booking
        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfGuests(request.getNumberOfGuests())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .specialRequests(request.getSpecialRequests())
                .bookingDate(LocalDateTime.now())
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking created successfully with ID: {}", booking.getId());

        return booking;
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy booking với ID: " + id));
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getRoomBookings(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    @Transactional
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        log.info("Updating booking {} to status {}", id, status);
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id, String reason) {
        log.info("Cancelling booking {}", id);
        Booking booking = getBookingById(id);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException("Booking đã được hủy trước đó");
        }

        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            throw new BookingException("Không thể hủy booking đã check-out");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(reason);
        bookingRepository.save(booking);

        log.info("Booking {} cancelled successfully", id);
    }

    @Override
    @Transactional
    public Booking checkIn(Long id) {
        log.info("Check-in for booking {}", id);
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BookingException("Chỉ có thể check-in với booking đã CONFIRMED");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);

        // Cập nhật room status
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.OCCUPIED);

        booking = bookingRepository.save(booking);
        log.info("Check-in successful for booking {}", id);

        return booking;
    }

    @Override
    @Transactional
    public Booking checkOut(Long id) {
        log.info("Check-out for booking {}", id);
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            throw new BookingException("Chỉ có thể check-out với booking đã CHECKED_IN");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);

        // Cập nhật room status về CLEANING
        Room room = booking.getRoom();
        room.setStatus(RoomStatus.CLEANING);

        booking = bookingRepository.save(booking);
        log.info("Check-out successful for booking {}", id);

        return booking;
    }
}