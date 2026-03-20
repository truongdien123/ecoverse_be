package com.fpt.ecoverse_backend.enums;

public enum PartnerStatus {
    PENDING("Đang chờ duyệt"),
    APPROVED("Đã duyệt"),
    REJECTED("Bị từ chối");

    private final String displayName;

    PartnerStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
