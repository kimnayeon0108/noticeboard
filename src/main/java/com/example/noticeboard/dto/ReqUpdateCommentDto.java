package com.example.noticeboard.dto;

import lombok.Getter;

@Getter
public class ReqUpdateCommentDto {

    private long userId;    // 로그인 구현 이후 삭제
    private String body;
}
