package com.nguyenhuuquang.hotelmanagement.dto.request;

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
public class UpdateRoomRequest {

    private String roomNumber;

    private Long roomTypeId;

    private Integer floorNumber;

    private String viewDescription;

    private BigDecimal currentPrice;

    private RoomStatus status;

    private Boolean isSmoking;

    private Boolean hasBalcony;

    private List<String> amenities;

    private List<String> imageUrls;
}