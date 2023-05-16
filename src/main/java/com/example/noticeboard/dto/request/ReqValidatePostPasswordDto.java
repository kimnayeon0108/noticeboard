package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Schema(description = "게시글 비밀번호 검증 DTO")
public class ReqValidatePostPasswordDto {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "영문, 숫자 포함 길이가 4이상 8이하이어야 합니다.")
    @Length(min = 4, max = 8)
    @Schema(description = "게시글 비밀번호", required = true, minLength = 4, maxLength = 8)
    private String password;
}
