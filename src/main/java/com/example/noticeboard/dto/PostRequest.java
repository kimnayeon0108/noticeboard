package com.example.noticeboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    private Long userId;        // 로그인 구현 이후 삭제 예정

    @JsonProperty("isPublic")
    private boolean isPublic;

    @JsonProperty("isCommentActive")
    private boolean isCommentActive;

    private String password;       // password 없으면 null
    private Long categoryId;
    private String title;
    private String body;
}
