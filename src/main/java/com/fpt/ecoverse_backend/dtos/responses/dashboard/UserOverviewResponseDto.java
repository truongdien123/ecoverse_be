package com.fpt.ecoverse_backend.dtos.responses.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOverviewResponseDto {
    private long totalStudents;
    private long totalParents;
    private long totalPartners;
}
