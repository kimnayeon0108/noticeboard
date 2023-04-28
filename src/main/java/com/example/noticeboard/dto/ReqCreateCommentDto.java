package com.example.noticeboard.dto;

import lombok.Getter;

@Getter
public class ReqCreateCommentDto {

    private Long userId;
    private String body;
    private Long parentCommentId;
}
