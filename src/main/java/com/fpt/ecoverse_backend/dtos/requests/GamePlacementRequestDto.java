package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamePlacementRequestDto {
    @JsonProperty("waste_item_id")
    private String wasteItemId;

    @JsonProperty("code")
    private WasteBinCode code;

    @JsonProperty("is_correct")
    private Boolean isCorrect;
}
