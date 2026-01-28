package com.nguyenhuuquang.hotelmanagement.service;

import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Review;

public interface ReviewService {
    Review createReview(Review review);

    Review updateReview(Long id, Review review);

    void deleteReview(Long id);

    Optional<Review> getReviewById(Long id);

    List<Review> getAllReviews();

    List<Review> getReviewsByBookingId(Long bookingId);

    List<Review> getReviewsByRoomId(Long roomId);

    List<Review> getApprovedReviews();

    Review approveReview(Long id);
}