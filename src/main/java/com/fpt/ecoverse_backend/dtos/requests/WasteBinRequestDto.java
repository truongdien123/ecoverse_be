package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WasteBinRequestDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("color_hex")
    private String colorHex;

    @JsonProperty("description")
    private String description;

    @JsonProperty("icon")
    private MultipartFile icon;
}