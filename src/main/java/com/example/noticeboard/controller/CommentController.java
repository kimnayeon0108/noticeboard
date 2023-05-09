package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqDeleteCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts/{postId}/comments")
@Tag(name = "comment", description = "댓글 api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "댓글 작성 api", tags = "comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<List<ResCommentDto>> addComment(
            @Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
            @Valid @RequestBody ReqCreateCommentDto reqCreateCommentDto) {

        // 댓글 작성 후 해당 게시글의 댓글 목록 반환
        return ResponseDto.success(commentService.addComment(postId, reqCreateCommentDto));
    }

    @GetMapping
    @Operation(summary = "게시글의 댓글 목록 조회", description = "댓글 목록 조회 api", tags = "comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<ResCommentDto>> showCommentList(
            @Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId) {

        return ResponseDto.success(commentService.getComments(postId));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글 수정 api", tags = "comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<List<ResCommentDto>> editComment(
            @Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
            @Parameter(in = ParameterIn.PATH, description = "댓글 id") @PathVariable long commentId,
            @Valid @RequestBody ReqUpdateCommentDto reqUpdateCommentDto) {

        return ResponseDto.success(commentService.updateComment(postId, commentId, reqUpdateCommentDto));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 삭제 api", tags = "comment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseDto<Void> deleteComment(
            @Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
            @Parameter(in = ParameterIn.PATH, description = "댓글 id") @PathVariable long commentId,
            @Valid @RequestBody ReqDeleteCommentDto reqDeleteCommentDto) {

        commentService.deleteComment(postId, commentId, reqDeleteCommentDto);
        return ResponseDto.success(null);
    }
}
