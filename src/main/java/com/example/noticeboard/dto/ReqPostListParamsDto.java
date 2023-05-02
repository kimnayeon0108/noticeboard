package com.example.noticeboard.dto;

import com.example.noticeboard.type.PostOrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPostListParamsDto {

    private String writer;
    private String title;
    private String body;
    private String categoryName;

    @Schema(description = "정렬 기준", nullable = false, allowableValues = {"CREATED_AT", "UPDATED_AT", "VIEW_COUNT"})
    private PostOrderType postOrder = PostOrderType.CREATED_AT;

    @Schema(description = "페이지", defaultValue = "1")
    private int page = 1;

    @Schema(description = "페이지 size", defaultValue = "20")
    private int pageSize = 20;
}
