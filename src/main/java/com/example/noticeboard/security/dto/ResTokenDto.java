package com.example.noticeboard.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResTokenDto {

    private String accessToken;
    private String refreshToken;
}
