package com.fpt.ecoverse_backend.dtos;

import lombok.Value;

@Value
public class RowResult<T> {
    int rowNumber;
    T data;
    boolean success;
    String message;

    public static <T> RowResult<T> success(int rowNumber, T data, String message) {
        return new RowResult<T>(rowNumber, data, true, message);
    }

    public static <T> RowResult<T> failed(int rowNumber, T data, String message) {
        return new RowResult<T>(rowNumber, data, false, message);
    }
}
