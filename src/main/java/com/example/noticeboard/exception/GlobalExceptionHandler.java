package com.example.noticeboard.exception;

import com.example.noticeboard.dto.response.ResErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResErrorDto handleBaseException(BaseException e) {
        return ResErrorDto.builder().errorCode(e.getErrorCode()).message(e.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : e.getFieldErrors()) {
            builder.append(fieldError.getField());
            builder.append("는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append("\n");
        }

        return ResErrorDto.builder().errorCode(ErrorCode.INVALID_REQUEST.getCode()).message(builder.toString()).build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResErrorDto handleBindException(BindException e) {

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            builder.append(fieldError.getField());
            builder.append("는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" \n ");
        }

        return ResErrorDto.builder().errorCode(ErrorCode.INVALID_REQUEST.getCode()).message(builder.toString()).build();
    }
}
