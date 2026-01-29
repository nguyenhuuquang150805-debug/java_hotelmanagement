package com.nguyenhuuquang.hotelmanagement.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.nguyenhuuquang.hotelmanagement.entity.enums.RoomStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {

    @NotBlank(message = "Số phòng không được để trống")
    private String roomNumber;

    @NotNull(message = "Loại phòng không được để trống")
    private Long roomTypeId;

    @Positive(message = "Số tầng phải lớn hơn 0")
    private Integer floorNumber;

    private String viewDescription;

    @NotNull(message = "Giá phòng không được để trống")
    @Positive(message = "Giá phòng phải lớn hơn 0")
    private BigDecimal currentPrice;

    private RoomStatus status;

    private Boolean isSmoking;

    private Boolean hasBalcony;

    private List<String> amenities;

    private List<String> imageUrls;
}