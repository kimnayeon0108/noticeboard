package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {

    private T data;
    private ErrorResponse error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(data, null);
    }
}