package com.nguyenhuuquang.hotelmanagement.entity.enums;

public enum RoomStatus implements StatusEnum {
    AVAILABLE("Có sẵn", "Room is available for booking"),
    OCCUPIED("Đang sử dụng", "Room is currently occupied"),
    RESERVED("Đã đặt trước", "Room is reserved"),
    MAINTENANCE("Bảo trì", "Room is under maintenance"),
    CLEANING("Đang dọn dẹp", "Room is being cleaned"),
    OUT_OF_SERVICE("Ngừng phục vụ", "Room is out of service");

    private final String displayName;
    private final String description;

    RoomStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return description;
    }
}