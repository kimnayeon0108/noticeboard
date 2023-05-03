package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Schema(description = "댓글 삭제 DTO")
public class ReqDeleteCommentDto { // Todo:로그인 구현 이후 삭제

    @NotNull
    @Schema(description = "유저 id")
    private long userId;
}
