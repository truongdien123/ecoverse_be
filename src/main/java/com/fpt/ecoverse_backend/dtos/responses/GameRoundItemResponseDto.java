package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoundItemResponseDto {

    @JsonProperty("order_index")
    private Integer orderIndex;

    @JsonProperty("waste_item_id")
    private String wasteItemId;
}
