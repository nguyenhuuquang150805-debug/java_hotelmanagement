package com.nguyenhuuquang.hotelmanagement.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private Long id;

    private String roomNumber;

    private RoomTypeInfo roomType;

    private Integer floorNumber;

    private String viewDescription;

    private BigDecimal currentPrice;

    private RoomStatus status;

    private Boolean isSmoking;

    private Boolean hasBalcony;

    private Double rating;

    private Integer totalReviews;

    private List<String> amenities;

    private List<String> imageUrls;

    private Boolean isAvailable;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomTypeInfo {
        private Long id;
        private String name;
        private Integer capacity;
        private String description;
        private BigDecimal basePrice;
    }
}