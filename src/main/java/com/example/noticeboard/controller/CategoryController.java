package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.ReqCreateCategoryDto;
import com.example.noticeboard.dto.request.ReqUpdateCategoryDto;
import com.example.noticeboard.dto.response.ResCategoryDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Tag(name = "category", description = "카테고리 api")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "카테고리 생성 api, 관리자 계정만 호출 가능", tags = "category")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResCategoryDto> addCategory(@RequestBody ReqCreateCategoryDto reqCreateCategoryDto) {

        return ResponseDto.success(categoryService.addCategory(reqCreateCategoryDto));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 이름 수정", description = "카테고리 수정 api, 관리자 계정만 호출 가능", tags = "category")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResCategoryDto> editCategory(@PathVariable long categoryId,
                                                    @RequestBody ReqUpdateCategoryDto reqUpdateCategoryDto) {

        return ResponseDto.success(categoryService.editCategory(categoryId, reqUpdateCategoryDto));
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제 api, 관리자 계정만 호출 가능, 하위 카테고리까지 모두 삭제됨, 게시글이 없어야 삭제 가능",
               tags = "category")
    public ResponseDto<Void> deleteCategory(@PathVariable long categoryId) {

        categoryService.deleteCategory(categoryId);
        return ResponseDto.success(null);
    }
}
