package com.nguyenhuuquang.hotelmanagement.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
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
public class CreateBookingRequest {

    @NotNull(message = "Room ID không được để trống")
    private Long roomId;

    @NotNull(message = "Ngày check-in không được để trống")
    @Future(message = "Ngày check-in phải là ngày trong tương lai")
    private LocalDate checkInDate;

    @NotNull(message = "Ngày check-out không được để trống")
    @Future(message = "Ngày check-out phải là ngày trong tương lai")
    private LocalDate checkOutDate;

    @NotNull(message = "Số lượng khách không được để trống")
    @Positive(message = "Số lượng khách phải lớn hơn 0")
    private Integer numberOfGuests;

    private String specialRequests;
}