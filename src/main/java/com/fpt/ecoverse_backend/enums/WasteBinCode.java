package com.fpt.ecoverse_backend.enums;

public enum WasteBinCode {
    PLASTIC("Thùng nhựa & kim loại"),
    PAPER("Thùng giấy"),
    ORGANIC("Thùng hữu cơ"),
    OTHERS("Thùng khác");

    private final String displayName;

    WasteBinCode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
