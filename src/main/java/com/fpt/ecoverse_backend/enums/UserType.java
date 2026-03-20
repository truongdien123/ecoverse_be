package com.fpt.ecoverse_backend.enums;

public enum UserType {
    STUDENT("Học sinh"),
    PARENT("Phụ huynh"),
    PARTNERSHIP("Đơn vị hợp tác"),
    ADMIN("Quản trị viên");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
