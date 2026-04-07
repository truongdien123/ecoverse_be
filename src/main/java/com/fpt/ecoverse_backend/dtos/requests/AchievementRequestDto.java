package com.fpt.ecoverse_backend.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequestDto {

    @NotBlank(message = "Tên thành tựu không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Số điểm yêu cầu không được để trống")
    @Min(value = 0, message = "Số điểm yêu cầu phải lớn hơn hoặc bằng 0")
    private Integer pointsRequired;

    private String badgeImageUrl;
}
