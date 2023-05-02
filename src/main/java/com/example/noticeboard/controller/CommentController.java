package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqDeleteCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@ResponseStatus(HttpStatus.OK)
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseDto<List<ResCommentDto>> addComment(@PathVariable long postId, @RequestBody ReqCreateCommentDto reqCreateCommentDto) {

        // 댓글 작성 후 해당 게시글의 댓글 목록 반환
        return ResponseDto.success(commentService.addComment(postId, reqCreateCommentDto));
    }

    @GetMapping
    public ResponseDto<List<ResCommentDto>> showCommentList(@PathVariable long postId) {
        return ResponseDto.success(commentService.getComments(postId));
    }

    @PutMapping("/{commentId}")
    public ResponseDto<List<ResCommentDto>> editComment(@PathVariable long postId, @PathVariable long commentId, @RequestBody ReqUpdateCommentDto reqUpdateCommentDto) {
        return ResponseDto.success(commentService.updateComment(postId, commentId, reqUpdateCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseDto<Void> deleteComment(@PathVariable long postId, @PathVariable long commentId, @RequestBody ReqDeleteCommentDto reqDeleteCommentDto) {
        commentService.deleteComment(postId, commentId, reqDeleteCommentDto);
        return ResponseDto.success(null);
    }
}
