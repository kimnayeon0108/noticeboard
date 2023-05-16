package com.example.noticeboard.handler;

import com.example.noticeboard.dto.response.ResErrorDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALID_ANNOTATION_EXCEPTION_MESSAGE = "파라미터 혹은 요청 body 값 검증 실패";

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleBaseException(BaseException e) {
        ResErrorDto errorDto = ResErrorDto.builder().errorCode(e.getErrorCode()).message(e.getMessage()).build();
        return ResponseDto.fail(errorDto);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleMethodArgumentNotValidException(BindException e) {     // BindException는 MethodArgumentNotValidException의 조상

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        ResErrorDto errorDto = ResErrorDto.builder().message(VALID_ANNOTATION_EXCEPTION_MESSAGE).errors(errors).build();
        return ResponseDto.fail(errorDto);
    }
}
