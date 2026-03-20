package com.fpt.ecoverse_backend.enums;

public enum ApprovalStatus {
    PENDING("Đang chờ"),
    APPROVED("Đã phê duyệt"),
    REJECTED("Bị từ chối");

    private final String displayName;

    ApprovalStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
