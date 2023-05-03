package com.example.noticeboard.dto.request;

import com.example.noticeboard.type.PostOrderType;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springdoc.api.annotations.ParameterObject;

@Getter
@Setter
@ParameterObject
public class ReqPostListParamsDto {

    @Parameter(description = "작성자 이름 (필터)")
    private String writerName;

    @Parameter(description = "제목 (검색)")
    private String title;

    @Parameter(description = "내용 (검색)")
    private String body;

    @Parameter(description = "카테고리 이름 (필터)")
    private String categoryName;

    @Parameter(required = true, description = "정렬 기준")
    @NonNull
    private PostOrderType orderBy = PostOrderType.CREATED_AT;

    @Parameter(required = true, description = "페이지")
    private int page = 1;

    @Parameter(required = true, description = "페이지 size")
    private int pageSize = 20;
}
