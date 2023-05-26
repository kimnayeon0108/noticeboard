package com.example.noticeboard.security.exception;

import com.example.noticeboard.exception.ErrorCode;
import org.springframework.security.core.AuthenticationException;

public class BaseAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public BaseAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode.getCode();
    }

    public String getErrorMessage() {
        return this.errorCode.getMessage();
    }
}
