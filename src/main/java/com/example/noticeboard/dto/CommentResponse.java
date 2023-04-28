package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponse {

    private String body;
    private String writerName;
    private List<CommentResponse> children = new ArrayList<>();

    public CommentResponse(String body, String writerName) {
        this.body = body;
        this.writerName = writerName;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(comment.getBody(), comment.getUser().getName());
    }
}
