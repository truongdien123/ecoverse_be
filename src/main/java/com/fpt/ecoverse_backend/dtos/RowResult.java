package com.fpt.ecoverse_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RowResult<T> {
    private final int rowNumber;
    private final T data;
    private final boolean success;
    private final String message;

    public static <T> RowResult<T> success(int rowNumber, T data, String message) {
        return new RowResult<>(rowNumber, data, true, message);
    }

    public static <T> RowResult<T> failed(int rowNumber, T data, String message) {
        return new RowResult<>(rowNumber, data, false, message);
    }
}
