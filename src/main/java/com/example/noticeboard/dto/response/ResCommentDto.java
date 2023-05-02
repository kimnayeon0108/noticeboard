package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResCommentDto {

    private long id;
    private String body;
    private String writerName;
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
