package com.example.noticeboard.controller;

import com.example.noticeboard.dto.*;
import com.example.noticeboard.dto.ResponseBody;
import com.example.noticeboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostController extends ApiV1Controller {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ResponseBody<PostResponse>> addPost(
            @RequestPart(value = "postRequest") PostRequest postRequest,
            @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) throws IOException {
        PostResponse postResponse = postService.addPost(postRequest, multipartFiles);
        return new ResponseEntity<>(ResponseBody.success(postResponse), HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<ResponseBody<PagingResponse<PostResponse>>> showPostList(PostListRequest postListRequest) {
        return new ResponseEntity<>(ResponseBody.success(postService.getPosts(postListRequest)), HttpStatus.OK);
    }

    //Todo: 비밀번호 체크 api  -> HTTP method POST가 맞을까...? 리소스 추가가 아닌데, url도 이게 맞을까..?
    @PostMapping("/posts/{postId}/password/validate")
    public ResponseEntity<ResponseBody<Boolean>> validatePassword(@PathVariable long postId, @RequestBody PostPasswordRequest postPasswordRequest) {
        return new ResponseEntity<>(ResponseBody.success(postService.validatePassword(postId, postPasswordRequest)), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseBody<PostResponse>> showPostDetail(@PathVariable long postId) {
        return new ResponseEntity<>(ResponseBody.success(postService.getPost(postId)), HttpStatus.OK);
    }
}
