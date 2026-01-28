package com.nguyenhuuquang.hotelmanagement.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nguyenhuuquang.hotelmanagement.entity.Promotion;
import com.nguyenhuuquang.hotelmanagement.repository.PromotionRepository;
import com.nguyenhuuquang.hotelmanagement.service.PromotionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @Override
    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    @Transactional
    public Promotion updatePromotion(Long id, Promotion promotion) {
        return promotionRepository.findById(id)
                .map(existing -> {
                    promotion.setId(id);
                    return promotionRepository.save(promotion);
                })
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    @Override
    public Optional<Promotion> getPromotionById(Long id) {
        return promotionRepository.findById(id);
    }

    @Override
    public Optional<Promotion> getPromotionByCode(String promotionCode) {
        return promotionRepository.findByPromotionCode(promotionCode);
    }

    @Override
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public List<Promotion> getActivePromotions() {
        return promotionRepository.findByIsActive(true);
    }

    @Override
    public List<Promotion> getValidPromotions(LocalDate date) {
        return promotionRepository.findByStartDateBeforeAndEndDateAfter(date, date);
    }

    @Override
    @Transactional
    public Promotion incrementUsageCount(Long id) {
        return promotionRepository.findById(id)
                .map(promotion -> {
                    promotion.setUsedCount(promotion.getUsedCount() + 1);
                    return promotionRepository.save(promotion);
                })
                .orElseThrow(() -> new RuntimeException("Promotion not found with id: " + id));
    }
}
