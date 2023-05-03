package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Schema(description = "게시글 비밀번호 검증 DTO")
public class ReqValidatePostPasswordDto {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Length(min = 4, max = 8)
    @Schema(description = "게시글 비밀번호", required = true, minLength = 4, maxLength = 8)
    private String password;
}
