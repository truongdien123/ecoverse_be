package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.CreatedBy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoundResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("shared")
    private Boolean shared;

    @JsonProperty("created_by")
    private CreatedBy createdBy;

    @JsonProperty("item_count")
    private Integer itemCount;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("game_round_items")
    private List<GameRoundItemResponseDto> gameRoundItems;
}
