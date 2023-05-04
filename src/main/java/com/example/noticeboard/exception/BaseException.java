package com.example.noticeboard.exception;

public class BaseException extends RuntimeException {

    private ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
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
