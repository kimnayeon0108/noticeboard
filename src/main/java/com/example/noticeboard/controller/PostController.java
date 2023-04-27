package com.example.noticeboard.controller;

import com.example.noticeboard.dto.PostDto;
import com.example.noticeboard.dto.PostRequest;
import com.example.noticeboard.dto.ResponseBody;
import com.example.noticeboard.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class PostController extends ApiV1Controller {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<ResponseBody<PostDto>> addPost(
            @RequestPart("post") PostRequest postRequest,
            @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles) throws IOException {
        PostDto postDto = postService.addPost(postRequest, multipartFiles);
        return new ResponseEntity<>(ResponseBody.success(postDto), HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity<ResponseBody<PagingResponse<PostDto>>> showPostList(PostListRequest postListRequest) {
        return new ResponseEntity<>(ResponseBody.success(postService.getPosts(postListRequest)), HttpStatus.OK);
    }
}
