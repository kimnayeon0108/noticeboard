package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "댓글 작성 DTO")
public class ReqCreateCommentDto {

    @NotNull
    @Schema(description = "유저 id")
    private long userId;

    @NotEmpty
    @Length(min = 1, max = 300)
    @Schema(description = "내용", minLength = 1, maxLength = 300)
    private String body;

    @Schema(description = "답글인 경우 부모 댓글 id를 입력하세요.")
    private Long parentCommentId;
}
