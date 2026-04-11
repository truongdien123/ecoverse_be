package com.fpt.ecoverse_backend.projections;

public interface LeaderboardProjection {
    String getStudentId();
    String getStudentName();
    String getGrade();
    Integer getPoints();
    Integer getMinDuration();
}
