package com.example.noticeboard.dto;

import lombok.Getter;

@Getter
public class CommentRequest {

    private Long userId;
    private String body;
    private Long parentCommentId;
}
