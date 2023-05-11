package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.ReqSignupDto;
import com.example.noticeboard.dto.response.ResUserDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResUserDto> signup(@Valid @RequestBody ReqSignupDto reqSignupDto) {
        return ResponseDto.success(userService.signup(reqSignupDto));
    }
}
