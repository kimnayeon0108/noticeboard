package com.example.noticeboard.service;

import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqDeleteCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.CommentRepository;
import com.example.noticeboard.repository.PostRepository;
import com.example.noticeboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<ResCommentDto> addComment(long postId, ReqCreateCommentDto reqCreateCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(reqCreateCommentDto.getUserId())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        if (!post.isCommentActiveState()) {
            throw new BaseException(ErrorCode.INACTIVE_COMMENT_POST);
        }

        Comment comment = new Comment(post, user, reqCreateCommentDto.getBody());

        // parentComment id가 있다면 조회
        if (reqCreateCommentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(reqCreateCommentDto.getParentCommentId())
                    .orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

            if (!comment.getPost().isSamePost(parentComment.getPost().getId())) {
                throw new BaseException(ErrorCode.POST_NOT_MATCH);
            }
            comment.updateParentComment(parentComment);
        }
        commentRepository.save(comment);

        return getComments(postId);
    }

    @Transactional(readOnly = true)
    public List<ResCommentDto> getComments(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));     // 게시글 존재 여부 검증
        List<Comment> comments = commentRepository.findAllByPostIdOrderByDepth(post.getId());

        List<ResCommentDto> resCommentDtos = new ArrayList<>();
        Map<Long, ResCommentDto> map = new HashMap<>();
        comments.forEach(comment -> {
            ResCommentDto resCommentDto = ResCommentDto.of(comment);
            map.put(comment.getId(), resCommentDto);

            // 부모 댓글이 있으면
            if (comment.getParentComment() != null) {
                ResCommentDto parentCommentDto = map.get(comment.getParentComment().getId());
                parentCommentDto.addChild(resCommentDto);
            } else {
                resCommentDtos.add(resCommentDto);
            }
        });
        return resCommentDtos;
    }

    public List<ResCommentDto> updateComment(long postId, long commentId, ReqUpdateCommentDto reqUpdateCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(reqUpdateCommentDto.getUserId())
                .orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost().isSamePost(post.getId())) {
            throw new BaseException(ErrorCode.POST_NOT_MATCH);
        }

        if (!comment.getUser().isWriter(user.getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        comment.updateBody(reqUpdateCommentDto.getBody());

        return getComments(postId);
    }

    public void deleteComment(long postId, long commentId, ReqDeleteCommentDto reqDeleteCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));
        User user = userRepository.findById(reqDeleteCommentDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글 미존재"));

        if (!comment.getPost().isSamePost(post.getId())) {
            throw new BaseException(ErrorCode.POST_NOT_MATCH);
        }

        if (!comment.getUser().isWriter(user.getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        if (comment.hasChildren()) {
            throw new BaseException(ErrorCode.UNDELETABLE_COMMENT);
        }

        commentRepository.deleteById(commentId);
    }
}
