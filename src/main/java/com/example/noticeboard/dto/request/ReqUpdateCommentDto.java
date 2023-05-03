package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Schema(description = "댓글 수정 DTO")
public class ReqUpdateCommentDto {

    @NotNull
    @Schema(description = "유저 id")
    private long userId;    // 로그인 구현 이후 삭제

    @NotEmpty
    @Length(min = 1, max = 300)
    @Schema(description = "내용", minLength = 1, maxLength = 300)
    private String body;
}
