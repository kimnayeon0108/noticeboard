package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class ReqCreateCategoryDto {

    @NotBlank
    @Schema(description = "카테고리 이름", maxLength = 10)
    @Length(max = 10)
    private String name;

    @Schema(description = "부모 카테고리 id, 최상위 카테고리인 경우 생략합니다.", nullable = true)
    private Long parentId;
}
