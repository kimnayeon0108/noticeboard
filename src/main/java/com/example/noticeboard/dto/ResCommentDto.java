package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResCommentDto {

    private String body;
    private String writerName;
    private List<ResCommentDto> children = new ArrayList<>();

    public ResCommentDto(String body, String writerName) {
        this.body = body;
        this.writerName = writerName;
    }

    public static ResCommentDto of(Comment comment) {
        return new ResCommentDto(comment.getBody(), comment.getUser().getName());
    }
}
