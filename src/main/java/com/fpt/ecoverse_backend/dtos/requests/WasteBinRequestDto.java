package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fpt.ecoverse_backend.enums.WasteBinCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WasteBinRequestDto {

    private WasteBinCode code;
    private String displayName;
    private String colorHex;
    private String description;
    private MultipartFile icon;
}