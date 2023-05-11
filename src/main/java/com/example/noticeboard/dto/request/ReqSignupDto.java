package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class ReqSignupDto {

    @Pattern(regexp = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 아닙니다.")
    @Schema(description = "이메일", example = "nykim@dkargo.io")
    @NotBlank
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
             message = "비밀번호는 영문, 특수문자, 숫자를 포함하고 8자 이상 20자 이하이어야 합니다.")
    @Schema(description = "비밀번호 (영문, 특수문자, 숫자를 포함하고 8자 이상 20자 이하)")
    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
