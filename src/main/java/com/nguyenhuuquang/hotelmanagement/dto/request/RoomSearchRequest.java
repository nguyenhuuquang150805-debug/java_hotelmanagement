package com.nguyenhuuquang.hotelmanagement.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSearchRequest {

    @NotNull(message = "Ngày check-in không được để trống")
    private LocalDate checkInDate;

    @NotNull(message = "Ngày check-out không được để trống")
    private LocalDate checkOutDate;

    private Long roomTypeId;

    private Integer numberOfGuests;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Boolean isSmoking;

    private Boolean hasBalcony;

    private Integer floorNumber;

    private String viewDescription;
}