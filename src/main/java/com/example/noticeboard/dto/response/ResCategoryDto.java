package com.example.noticeboard.dto.response;

import com.example.noticeboard.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@Schema(description = "카테고리 응답 DTO")
public class ResCategoryDto {

    @Schema(description = "id")
    private long id;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "1: 대분류, 2: 중분류, 3: 소분류")
    private int depth;

    @Schema(description = "생성 시간")
    private LocalDateTime createdAt;

    public static ResCategoryDto of(Category category) {
        return ResCategoryDto.builder()
                             .id(category.getId())
                             .name(category.getName())
                             .depth(category.getDepth())
                             .createdAt(category.getCreatedAt())
                             .build();
    }
}
