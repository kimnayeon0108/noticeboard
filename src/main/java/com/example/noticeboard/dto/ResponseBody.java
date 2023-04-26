package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseBody<T> {

    private T date;
    private ErrorResponse error;

    public static <T> ResponseBody<T> success(T data) {
        return new ResponseBody<>(data, null);
    }
}
