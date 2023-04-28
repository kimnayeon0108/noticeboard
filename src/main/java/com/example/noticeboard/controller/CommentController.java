package com.example.noticeboard.controller;

import com.example.noticeboard.dto.CommentRequest;
import com.example.noticeboard.dto.CommentResponse;
import com.example.noticeboard.dto.ResponseDto;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController extends ApiV1Controller {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ResponseDto<List<CommentResponse>>> addComment(@PathVariable long postId, @RequestBody CommentRequest commentRequest) {

        // 댓글 작성 후 해당 게시글의 댓글 목록 반환
        return new ResponseEntity<>(ResponseDto.success(commentService.addComment(postId, commentRequest)), HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ResponseDto<List<CommentResponse>>> showCommentList(@PathVariable long postId) {
        return new ResponseEntity<>(ResponseDto.success(commentService.getComments(postId)), HttpStatus.OK);
    }
}
