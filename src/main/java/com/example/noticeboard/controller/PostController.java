package com.example.noticeboard.controller;

import com.example.noticeboard.dto.*;
import com.example.noticeboard.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@ResponseStatus(HttpStatus.OK)
@Tag(name = "post", description = "post api")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseDto<ResPostDto> addPost(@RequestPart(value = "post") ReqCreatePostDto reqCreatePostDto,
                                           @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) throws IOException {
        ResPostDto resPostDto = postService.addPost(reqCreatePostDto, multipartFiles);
        return ResponseDto.success(resPostDto);
    }

    @GetMapping
    public ResponseDto<ResPagingDto<ResPostDto>> showPostList(@ModelAttribute ReqPostListParamsDto reqPostListParamsDto) {
        return ResponseDto.success(postService.getPosts(reqPostListParamsDto));
    }

    //Todo: validate-password  HTTP method: patch...?
    @PostMapping("/{postId}/password/validate")
    public ResponseDto<Boolean> validatePassword(@PathVariable long postId, @RequestBody ReqValidatePostPasswordDto reqValidatePostPasswordDto) {
        return ResponseDto.success(postService.validatePassword(postId, reqValidatePostPasswordDto));
    }

    @GetMapping("/{postId}")
    public ResponseDto<ResPostDto> showPostDetail(@PathVariable long postId) {
        return ResponseDto.success(postService.getPost(postId));
    }

    @PutMapping("/{postId}")
    public ResponseDto<ResPostDto> editPost(@PathVariable long postId,
                                            @RequestPart(value = "post") ReqUpdatePostDto reqUpdatePostDto,
                                            @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) throws IOException {
        return ResponseDto.success(postService.updatePost(postId, reqUpdatePostDto, multipartFiles));
    }

    @DeleteMapping
    public ResponseDto<Void> deletePost(@RequestBody ReqPostDeleteDto reqDeleteDto) {
        postService.deletePost(reqDeleteDto);
        return ResponseDto.success(null);
    }
}
