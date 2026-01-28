package com.nguyenhuuquang.hotelmanagement.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nguyenhuuquang.hotelmanagement.entity.Promotion;
import com.nguyenhuuquang.hotelmanagement.service.PromotionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {
        private final PromotionService promotionService;

        @PostMapping
        public ResponseEntity<EntityModel<Promotion>> createPromotion(@RequestBody Promotion promotion) {
                Promotion createdPromotion = promotionService.createPromotion(promotion);
                EntityModel<Promotion> entityModel = toModel(createdPromotion);
                return ResponseEntity
                                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                                .body(entityModel);
        }

        @PutMapping("/{id}")
        public ResponseEntity<EntityModel<Promotion>> updatePromotion(@PathVariable Long id,
                        @RequestBody Promotion promotion) {
                Promotion updatedPromotion = promotionService.updatePromotion(id, promotion);
                return ResponseEntity.ok(toModel(updatedPromotion));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
                promotionService.deletePromotion(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/{id}")
        public ResponseEntity<EntityModel<Promotion>> getPromotionById(@PathVariable Long id) {
                return promotionService.getPromotionById(id)
                                .map(promotion -> ResponseEntity.ok(toModel(promotion)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/code/{code}")
        public ResponseEntity<EntityModel<Promotion>> getPromotionByCode(@PathVariable String code) {
                return promotionService.getPromotionByCode(code)
                                .map(promotion -> ResponseEntity.ok(toModel(promotion)))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping
        public ResponseEntity<CollectionModel<EntityModel<Promotion>>> getAllPromotions() {
                List<EntityModel<Promotion>> promotions = promotionService.getAllPromotions().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Promotion>> collectionModel = CollectionModel.of(promotions,
                                linkTo(methodOn(PromotionController.class).getAllPromotions()).withSelfRel(),
                                linkTo(methodOn(PromotionController.class).getActivePromotions()).withRel("active"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/active")
        public ResponseEntity<CollectionModel<EntityModel<Promotion>>> getActivePromotions() {
                List<EntityModel<Promotion>> promotions = promotionService.getActivePromotions().stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Promotion>> collectionModel = CollectionModel.of(promotions,
                                linkTo(methodOn(PromotionController.class).getActivePromotions()).withSelfRel(),
                                linkTo(methodOn(PromotionController.class).getAllPromotions()).withRel("all"));

                return ResponseEntity.ok(collectionModel);
        }

        @GetMapping("/valid")
        public ResponseEntity<CollectionModel<EntityModel<Promotion>>> getValidPromotions(
                        @RequestParam LocalDate date) {
                List<EntityModel<Promotion>> promotions = promotionService.getValidPromotions(date).stream()
                                .map(this::toModel)
                                .collect(Collectors.toList());

                CollectionModel<EntityModel<Promotion>> collectionModel = CollectionModel.of(promotions,
                                linkTo(methodOn(PromotionController.class).getValidPromotions(date)).withSelfRel(),
                                linkTo(methodOn(PromotionController.class).getAllPromotions()).withRel("all"));

                return ResponseEntity.ok(collectionModel);
        }

        @PatchMapping("/{id}/increment-usage")
        public ResponseEntity<EntityModel<Promotion>> incrementUsageCount(@PathVariable Long id) {
                Promotion promotion = promotionService.incrementUsageCount(id);
                return ResponseEntity.ok(toModel(promotion));
        }

        private EntityModel<Promotion> toModel(Promotion promotion) {
                EntityModel<Promotion> entityModel = EntityModel.of(promotion);

                entityModel.add(linkTo(methodOn(PromotionController.class).getPromotionById(promotion.getId()))
                                .withSelfRel());
                entityModel.add(linkTo(methodOn(PromotionController.class).getAllPromotions()).withRel("promotions"));
                entityModel.add(
                                linkTo(methodOn(PromotionController.class).updatePromotion(promotion.getId(), null))
                                                .withRel("update"));
                entityModel
                                .add(linkTo(methodOn(PromotionController.class).deletePromotion(promotion.getId()))
                                                .withRel("delete"));
                entityModel.add(linkTo(methodOn(PromotionController.class).incrementUsageCount(promotion.getId()))
                                .withRel("incrementUsage"));

                return entityModel;
        }
}