package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Schema(description = "댓글 응답 DTO")
public class ResCommentDto {

    @Schema(description = "id")
    private long id;

    @Schema(description = "내용")
    private String body;

    @Schema(description = "작성자 이름")
    private String writerName;

    @Schema(description = "답글 목록")
    private List<ResCommentDto> children = new ArrayList<>();

    public ResCommentDto(long id, String body, String writerName) {
        this.id = id;
        this.body = body;
        this.writerName = writerName;
    }

    public static ResCommentDto of(Comment comment) {
        return new ResCommentDto(comment.getId(), comment.getBody(), comment.getUser().getName());
    }
}
