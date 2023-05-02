package com.example.noticeboard.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReqPostDeleteDto {

    private long userId;  // 로그인 구현 이후 삭제
    private List<Long> postIds;
}
