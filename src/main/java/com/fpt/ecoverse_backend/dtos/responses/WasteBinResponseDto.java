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
public class WasteBinResponseDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("color_hex")
    private String colorHex;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private boolean active;
}

