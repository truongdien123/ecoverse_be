package com.fpt.ecoverse_backend.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RewardItemRequestDto {

    private String name;
    private String description;
    private Integer pointRequired;
    private MultipartFile image;
    private Boolean available;
}
