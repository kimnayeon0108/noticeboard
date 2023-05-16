package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Schema(description = "댓글 수정 DTO")
public class ReqUpdateCommentDto {

    @NotEmpty
    @Length(min = 1, max = 300)
    @Schema(description = "내용", minLength = 1, maxLength = 300)
    private String body;
}
