package com.fpt.ecoverse_backend.dtos.responses.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerPointResponseDto {
    private String partnerId;
    private String partnerName;
    private Long totalPoints;
}
