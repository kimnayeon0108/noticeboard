package com.example.noticeboard.dto;

import com.example.noticeboard.domain.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class PostDto {

    private String title;
    private String writerName;
    private String body;
    private String categoryName;
    private int viewCount;
    private List<String> filenames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PostDto of(Post post, List<String> filenames) {
        return PostDto.builder().title(post.getTitle())
                .writerName(post.getUser().getName())
                .body(post.getBody())
                .categoryName(post.getCategory().getName())
                .viewCount(post.getViewCount())
                .filenames(filenames)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
