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
public class RewardItemResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("point_required")
    private Integer pointRequired;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("partner_id")
    private String partnerId;

    @JsonProperty("available")
    private Boolean available;
}
