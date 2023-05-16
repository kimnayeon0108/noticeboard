package com.example.noticeboard.service;

import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.CommentRepository;
import com.example.noticeboard.repository.PostRepository;
import com.example.noticeboard.security.dto.UserDetailsDto;
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
    private final UserService userService;

    public List<ResCommentDto> addComment(long postId, ReqCreateCommentDto reqCreateCommentDto, UserDetailsDto userDetails) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userService.getUser(userDetails.getUsername());

        if (!post.isCommentActiveState()) {
            throw new BaseException(ErrorCode.INACTIVE_COMMENT_POST);
        }

        Comment comment = new Comment(post, user, reqCreateCommentDto.getBody());

        // parentComment id가 있다면 조회
        if (reqCreateCommentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(reqCreateCommentDto.getParentCommentId())
                                                     .orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

            if (!comment.getPost()
                        .isSamePost(parentComment.getPost()
                                                 .getId())) {
                throw new BaseException(ErrorCode.POST_NOT_MATCH);
            }
            comment.updateParentComment(parentComment);
        }
        commentRepository.save(comment);

        return getComments(postId);
    }

    @Transactional(readOnly = true)
    public List<ResCommentDto> getComments(long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));

        List<ResCommentDto> firstDepthCommentDtos = new ArrayList<>();

        Map<Long, ResCommentDto> commentIdToCommentDto = new HashMap<>();

        commentRepository.findAllByPostIdOrderByDepth(post.getId())
                         .forEach(comment -> {
                             ResCommentDto resCommentDto = ResCommentDto.of(comment);
                             commentIdToCommentDto.put(comment.getId(), resCommentDto);

                             // 부모 댓글이 있으면 commentIdToCommentDto map에서 꺼내서 dto의 childeren에 추가
                             if (comment.getParentComment() != null) {
                                 ResCommentDto parentCommentDto = commentIdToCommentDto.get(comment.getParentComment()
                                                                                                   .getId());
                                 parentCommentDto.addChild(resCommentDto);
                             } else {
                                 firstDepthCommentDtos.add(resCommentDto);
                             }
                         });
        return firstDepthCommentDtos;
    }

    public List<ResCommentDto> updateComment(long postId, long commentId, ReqUpdateCommentDto reqUpdateCommentDto,
                                             UserDetailsDto userDetails) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userService.getUser(userDetails.getUsername());
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost()
                    .isSamePost(post.getId())) {
            throw new BaseException(ErrorCode.POST_NOT_MATCH);
        }

        if (!comment.getUser()
                    .isWriter(user.getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        comment.updateBody(reqUpdateCommentDto.getBody());

        return getComments(postId);
    }

    public void deleteComment(long postId, long commentId, UserDetailsDto userDetails) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userService.getUser(userDetails.getUsername());
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new BaseException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getPost()
                    .isSamePost(post.getId())) {
            throw new BaseException(ErrorCode.POST_NOT_MATCH);
        }

        if (!comment.getUser()
                    .isWriter(user.getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        if (comment.hasChildren()) {
            throw new BaseException(ErrorCode.UNDELETABLE_COMMENT);
        }

        commentRepository.deleteById(commentId);
    }
}
