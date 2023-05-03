package com.example.noticeboard.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class ReqCreatePostDto {

    @NotNull
    @Parameter(description = "유저 id", required = true)
    private Long userId;        // 로그인 구현 이후 삭제 예정

    @JsonProperty("isPublic")
    @Parameter(description = "공개 여부", required = true)
    @NotNull
    private Boolean isPublic;

    @JsonProperty("isCommentActive")
    @Parameter(description = "댓글 사용 여부", required = true)
    @NotNull
    private Boolean isCommentActive;

    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Length(min = 4, max = 8)
    @Parameter(description = "게시글 비밀번호, 비밀번호 미설정 시 제외합니다.")
    private String password;

    @NotNull
    @Parameter(description = "카테고리 id", required = true)
    private Long categoryId;

    @NotEmpty
    @Length(min = 1, max = 30)
    @Parameter(description = "제목 (1 ~ 30자까지)", required = true)
    private String title;

    @NotEmpty
    @Length(min = 1, max = 1000)
    @Parameter(description = "내용 (1 ~ 1000자까지)", required = true)
    private String body;

    @Parameter(description = "파일 업로드 3개까지")
    MultipartFile[] multipartFiles;
}
