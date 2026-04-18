package com.fpt.ecoverse_backend.enums;

public enum CreatedBy {
    PARTNERSHIP("Đơn vị hợp tác"),
    ADMIN("Quản trị viên"),
    STUDENT("Học sinh");

    private final String displayName;

    CreatedBy(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
