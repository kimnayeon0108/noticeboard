package com.example.noticeboard.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public JwtAuthenticationToken(String principal, String credentials) {
        super(principal, credentials);
    }
}
