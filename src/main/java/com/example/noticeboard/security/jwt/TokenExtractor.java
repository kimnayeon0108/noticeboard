package com.example.noticeboard.security.jwt;

import org.springframework.stereotype.Component;

@Component
public class TokenExtractor {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }
}
