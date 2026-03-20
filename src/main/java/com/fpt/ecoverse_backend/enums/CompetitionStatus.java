package com.fpt.ecoverse_backend.enums;

public enum CompetitionStatus {
    DRAFT("Nháp"),
    ACTIVE("Đang hoạt động"),
    FINISHED("Đã kết thúc"),
    CANCELED("Đã hủy");

    private final String displayName;

    CompetitionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
