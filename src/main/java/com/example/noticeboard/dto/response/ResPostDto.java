package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.PostFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Schema(description = "게시글 응답 DTO")
public class ResPostDto {

    @Schema(description = "id")
    private long id;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "작성자 이름")
    private String writerName;

    @Schema(description = "내용")
    private String body;

    @Schema(description = "카테고리 이름")
    private String categoryName;

    @Schema(description = "조회 수")
    private int viewCount;

    @Schema(description = "첨부파일 이름 목록")
    private List<String> filenames;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    @Schema(description = "수정 시간")
    private LocalDateTime updatedAt;

    public static ResPostDto of(Post post, List<String> filenames) {
        return ResPostDto.builder().id(post.getId())
                .title(post.getTitle())
                .writerName(post.getUser().getName())
                .body(post.getBody())
                .categoryName(post.getCategory().getName())
                .viewCount(post.getViewCount())
                .filenames(filenames)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public static ResPostDto of(Post post) {
        return ResPostDto.builder().id(post.getId())
                .title(post.getTitle())
                .writerName(post.getUser().getName())
                .body(post.getBody())
                .categoryName(post.getCategory().getName())
                .viewCount(post.getViewCount())
                .filenames(post.getPostFiles().stream().map(PostFile::getFilename).collect(Collectors.toList()))
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
