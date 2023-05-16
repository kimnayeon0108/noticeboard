package com.example.noticeboard.dto.request;

import com.example.noticeboard.type.PostOrderType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class ReqPostListParamsDto {

    @Parameter(description = "작성자 id (필터)")
    private Long userId;

    @Parameter(description = "제목 (검색)")
    private String title;

    @Parameter(description = "내용 (검색)")
    private String body;

    @Parameter(description = "카테고리 id (필터)")
    private Long categoryId;

    @Parameter(description = "정렬 기준, 기본값: CREATED_AT")
    private PostOrderType orderBy = PostOrderType.CREATED_AT;

    @Parameter(description = "페이지, 기본값: 1")
    private int page = 1;

    @Parameter(description = "페이지 size, 기본값: 20")
    private int pageSize = 20;
}
