package com.nguyenhuuquang.hotelmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookingId(Long bookingId);

    List<Review> findByRoomId(Long roomId);

    List<Review> findByIsApproved(Boolean isApproved);

    List<Review> findByRoomIdAndIsApproved(Long roomId, Boolean isApproved);
}