package com.example.noticeboard.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ResErrorDto {

    private String errorCode;
    private String message;
    private Map<String, String> errors;  // @Valid 로 발생된 에러들
}
