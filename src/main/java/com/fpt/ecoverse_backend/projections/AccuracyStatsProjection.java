package com.fpt.ecoverse_backend.projections;

public interface AccuracyStatsProjection {

    Long getCorrectSum();

    Long getTotalSum();

    default Double getAccuracyPercentage() {
        Long totalSum = getTotalSum();
        if (totalSum == null || totalSum == 0) {
            return 0.0;
        }
        Long correctSum = getCorrectSum();
        if (correctSum == null) {
            return 0.0;
        }
        return (correctSum * 100.0) / totalSum;
    }
}
