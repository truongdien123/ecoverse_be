package com.fpt.ecoverse_backend.enums;

public enum LeaderboardScope {
    CLASS("Lớp học"),
    SCHOOL("Trường học");

    private final String displayName;

    LeaderboardScope(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
