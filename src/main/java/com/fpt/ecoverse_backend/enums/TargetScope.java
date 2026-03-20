package com.fpt.ecoverse_backend.enums;

public enum TargetScope {
    SCHOOL("Trường học"),
    CLASS("Lớp học");

    private final String displayName;

    TargetScope(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
