package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.*;
import com.example.noticeboard.dto.response.ResPagingDto;
import com.example.noticeboard.dto.response.ResPostDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@ResponseStatus(HttpStatus.OK)
@Tag(name = "post", description = "게시글 api")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성", description = "게시글 작성 api", tags = "post")
    public ResponseDto<ResPostDto> addPost(@Valid @ModelAttribute ReqCreatePostDto reqCreatePostDto) throws IOException {
        ResPostDto resPostDto = postService.addPost(reqCreatePostDto);
        return ResponseDto.success(resPostDto);
    }

    @GetMapping
    @Operation(summary = "게시글 리스트 조회", description = "게시글의 목록 조회 api", tags = "post")
    public ResponseDto<ResPagingDto<ResPostDto>> showPostList(@ModelAttribute ReqPostListParamsDto reqPostListParamsDto) {
        return ResponseDto.success(postService.getPosts(reqPostListParamsDto));
    }

    @PostMapping("/{postId}/password/validate")
    @Operation(summary = "게시글 비밀번호 검증", description = "게시글의 비밀번호 검증 api, 작성자가 아닌 유저가 게시글에 접근할 때 호출합니다.", tags = "post")
    public ResponseDto<Boolean> validatePassword(@Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
                                                 @Valid @RequestBody ReqValidatePostPasswordDto reqValidatePostPasswordDto) {
        return ResponseDto.success(postService.validatePassword(postId, reqValidatePostPasswordDto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회 api", tags = "post")
    public ResponseDto<ResPostDto> showPostDetail(@Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId) {
        return ResponseDto.success(postService.getPost(postId));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정", description = "게시글 수정 api", tags = "post")
    public ResponseDto<ResPostDto> editPost(@Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
                                            @Valid @ModelAttribute ReqUpdatePostDto reqUpdatePostDto) throws IOException {
        return ResponseDto.success(postService.updatePost(postId, reqUpdatePostDto));
    }

    @DeleteMapping
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 api", tags = "post")
    public ResponseDto<Void> deletePost(@Valid @RequestBody ReqDeletePostDto reqDeleteDto) {
        postService.deletePost(reqDeleteDto);
        return ResponseDto.success(null);
    }
}
