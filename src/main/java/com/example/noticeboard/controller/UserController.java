package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.ReqSignupDto;
import com.example.noticeboard.dto.response.ResUserDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.service.UserService;
import com.example.noticeboard.type.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입", description = "일반 회원용 가입 api", tags = "user")
    public ResponseDto<ResUserDto> signup(@Valid @RequestBody ReqSignupDto reqSignupDto) {
        return ResponseDto.success(userService.signup(reqSignupDto, UserRole.ROLE_USER));
    }

    @PostMapping("/admin/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "관리자 회원가입", description = "관리자용 가입 api", tags = "user")
    public ResponseDto<ResUserDto> adminSignup(@Valid @RequestBody ReqSignupDto reqSignupDto) {
        return ResponseDto.success(userService.signup(reqSignupDto, UserRole.ROLE_ADMIN));
    }

    @GetMapping("/user/profile")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "본인 정보 조회", description = "유저 정보 조회 api", tags = "user")
    public ResponseDto<ResUserDto> showProfile(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsDto userDetails) {
        return ResponseDto.success(userService.getProfile(userDetails.getUsername()));
    }
}
