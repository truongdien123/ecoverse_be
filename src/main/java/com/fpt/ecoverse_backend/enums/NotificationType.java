package com.fpt.ecoverse_backend.enums;

public enum NotificationType {
    ACHIEVEMENT("Thành tích"),
    REDEMPTION_REQUEST("Yêu cầu đổi thưởng"),
    REWARD_AVAILABLE("Thưởng khả dụng"),
    MILESTONE("Cột mốc"),
    UPDATE("Cập nhật");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
