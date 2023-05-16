package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "카테고리 이름 수정 DTO")
public class ReqUpdateCategoryDto {

    @NotBlank
    @Length(max = 10)
    @Schema(description = "카테고리 이름", maxLength = 10)
    private String name;
}
