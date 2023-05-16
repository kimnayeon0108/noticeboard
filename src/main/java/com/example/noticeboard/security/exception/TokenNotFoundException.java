package com.example.noticeboard.security.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenNotFoundException extends AuthenticationException {

    public TokenNotFoundException() {
        super("토큰이 존재하지 않습니다.");
    }
}
