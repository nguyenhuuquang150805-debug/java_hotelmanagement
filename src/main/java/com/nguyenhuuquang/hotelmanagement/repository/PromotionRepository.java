package com.nguyenhuuquang.hotelmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nguyenhuuquang.hotelmanagement.entity.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByPromotionCode(String promotionCode);

    List<Promotion> findByIsActive(Boolean isActive);

    List<Promotion> findByStartDateBeforeAndEndDateAfter(LocalDate start, LocalDate end);
}