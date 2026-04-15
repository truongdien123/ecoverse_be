package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionLinkRequestDto {

    @JsonProperty("quiz_template_id")
    private String quizTemplateId;

    @JsonProperty("game_round_id")
    private String gameRoundId;

    @JsonProperty("is_custom")
    private Boolean isCustom;

    @JsonProperty("score")
    private Integer score;
}
