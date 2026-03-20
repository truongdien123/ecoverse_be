package com.fpt.ecoverse_backend.enums;

public enum CompetitionType {
    QUIZ("Trắc nghiệm"),
    GAME("Trò chơi");

    private final String displayName;

    CompetitionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
