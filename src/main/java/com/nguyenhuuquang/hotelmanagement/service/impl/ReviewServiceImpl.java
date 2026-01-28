package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Review;
import com.nguyenhuuquang.hotelmanagement.repository.ReviewRepository;
import com.nguyenhuuquang.hotelmanagement.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public Review updateReview(Long id, Review review) {
        return reviewRepository.findById(id)
                .map(existing -> {
                    review.setId(id);
                    return reviewRepository.save(review);
                })
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> getReviewsByBookingId(Long bookingId) {
        return reviewRepository.findByBookingId(bookingId);
    }

    @Override
    public List<Review> getReviewsByRoomId(Long roomId) {
        return reviewRepository.findByRoomId(roomId);
    }

    @Override
    public List<Review> getApprovedReviews() {
        return reviewRepository.findByIsApproved(true);
    }

    @Override
    @Transactional
    public Review approveReview(Long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    review.setIsApproved(true);
                    return reviewRepository.save(review);
                })
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }
}