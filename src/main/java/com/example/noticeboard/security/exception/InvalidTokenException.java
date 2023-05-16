package com.example.noticeboard.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super("토큰이 유효하지 않습니다.");
    }
}
