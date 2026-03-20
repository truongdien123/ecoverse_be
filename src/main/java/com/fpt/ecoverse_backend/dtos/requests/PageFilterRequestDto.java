package com.fpt.ecoverse_backend.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageFilterRequestDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("page_no")
    private int pageNo;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("sorting")
    private String sorting;

    @JsonProperty("searching")
    private String searching;

    @JsonProperty("grade")
    private String grade;

    @JsonProperty("has_children")
    private boolean hasChildren;
}

