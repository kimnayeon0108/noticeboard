package com.example.noticeboard.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResErrorDto {

    private String errorCode;
    private String message;
}
