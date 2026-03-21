package com.fpt.ecoverse_backend.dtos;

import lombok.Value;

@Value
public class RowInput<T> {
    int excelRowNumber;
    T dto;
}
