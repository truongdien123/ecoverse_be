package com.fpt.ecoverse_backend.dtos.responses;

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
public class GamePlacementResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("waste_item")
    private WasteItemResponseDto wasteItem;

    @JsonProperty("code")
    private WasteBinCode code;

    @JsonProperty("is_correct")
    private Boolean isCorrect;
}
