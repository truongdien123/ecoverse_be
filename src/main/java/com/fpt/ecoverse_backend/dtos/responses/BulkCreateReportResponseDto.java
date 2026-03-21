package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkCreateReportResponseDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("total_rows")
    private int totalRows;

    @JsonProperty("successful")
    private int successful;

    @JsonProperty("failed")
    private int failed;

    @JsonProperty("processed_at")
    private LocalDateTime processedAt;

    @JsonProperty("report_file_url")
    private String reportFileUrl;

    @JsonProperty("report_file_name")
    private String reportFileName;

    @JsonProperty("expires_in")
    private long expiresIn;
}

