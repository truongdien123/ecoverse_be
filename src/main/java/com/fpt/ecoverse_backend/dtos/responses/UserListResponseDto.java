package com.fpt.ecoverse_backend.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponseDto<T> {
    @JsonProperty("list")
    private List<T> list;

    @JsonProperty("page")
    private int page;

    @JsonProperty("size")
    private int size;

    @JsonProperty("total_element")
    private long totalElement;

    @JsonProperty("total_page")
    private int totalPage;
}
