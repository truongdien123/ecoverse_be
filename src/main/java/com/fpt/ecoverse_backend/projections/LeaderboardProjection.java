package com.fpt.ecoverse_backend.projections;

public interface LeaderboardProjection {
    String getStudentId();
    String getStudentName();
    String getAvatarUrl();
    String getGrade();
    Integer getPoints();
    Integer getMinDuration();
}
