package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteItemResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("correct_bin_code")
    private WasteBinCode correctBinCode;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("order_index")
    private Integer orderIndex;
}
