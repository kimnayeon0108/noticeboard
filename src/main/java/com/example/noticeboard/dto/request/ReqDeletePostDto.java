package com.example.noticeboard.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Schema(description = "게시글 삭제 DTO")
@Getter
@Setter
public class ReqDeletePostDto {

    @NotNull
    @Schema(description = "유저 id")
    private long userId;  // 로그인 구현 이후 삭제

    @NotEmpty(message = "삭제할 게시글의 id를 입력해 주세요.")
    @Schema(description = "삭제할 게시글의 id 리스트")
    private Set<Long> postIds;
}
