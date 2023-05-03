package com.example.noticeboard.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "게시글 수정 DTO")
@Getter
public class ReqUpdatePostDto {

    @NotNull
    @Schema(description = "유저 id")
    private long userId;    // 로그인 구현 이후 삭제

    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Length(min = 4, max = 8)
    @Schema(description = "게시글 비밀번호, 비밀번호 미설정 시 제외합니다.")
    private String password;

    @JsonProperty("isPublic")
    @Schema(description = "공개 여부", defaultValue = "true")
    private boolean isPublic;

    @NotNull
    @Schema(description = "카테고리 id")
    private long categoryId;

    @NotEmpty
    @Length(min = 1, max = 30)
    @Schema(description = "제목", minLength = 1, maxLength = 30)
    private String title;

    @NotEmpty
    @Length(min = 1, max = 1000)
    @Schema(description = "내용", minLength = 1, maxLength = 1000)
    private String body;
}
