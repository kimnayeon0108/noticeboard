package com.example.noticeboard.dto.request;

import lombok.Getter;

@Getter
public class ReqUpdatePostDto {

    private long userId;    // 로그인 구현 이후 삭제
    private String password;
    private boolean isPublic;
    private long categoryId;
    private String title;
    private String body;
}
