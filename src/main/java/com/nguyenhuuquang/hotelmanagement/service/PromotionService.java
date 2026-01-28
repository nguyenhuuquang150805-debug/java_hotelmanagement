package com.nguyenhuuquang.hotelmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.nguyenhuuquang.hotelmanagement.entity.Promotion;

public interface PromotionService {
    Promotion createPromotion(Promotion promotion);

    Promotion updatePromotion(Long id, Promotion promotion);

    void deletePromotion(Long id);

    Optional<Promotion> getPromotionById(Long id);

    Optional<Promotion> getPromotionByCode(String promotionCode);

    List<Promotion> getAllPromotions();

    List<Promotion> getActivePromotions();

    List<Promotion> getValidPromotions(LocalDate date);

    Promotion incrementUsageCount(Long id);
}