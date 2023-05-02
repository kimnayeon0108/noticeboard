package com.example.noticeboard.service;

import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqDeleteCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));
        User user = userRepository.findById(reqCreateCommentDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));

        Comment comment = new Comment(post, user, reqCreateCommentDto.getBody());

        // parentComment id가 있다면 조회
        if (reqCreateCommentDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(reqCreateCommentDto.getParentCommentId()).orElseThrow(() -> new RuntimeException("댓글 미존재"));  //Todo: custom exception

            if (!comment.getPost().isSamePost(parentComment.getPost().getId())) {
                throw new RuntimeException("댓글의 게시글이 같아야 대댓글 작성 가능");
            }
            comment.updateParentComment(parentComment);
        }
        commentRepository.save(comment);

        return getComments(postId);
    }

    @Transactional(readOnly = true)
    public List<ResCommentDto> getComments(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));     // 게시글 존재 여부 검증
        List<Comment> comments = commentRepository.findAllByPostIdOrderByDepth(post.getId());

        List<ResCommentDto> resCommentDtos = new ArrayList<>();
        Map<Long, ResCommentDto> map = new HashMap<>();
        comments.forEach(comment -> {
            ResCommentDto resCommentDto = ResCommentDto.of(comment);
            map.put(comment.getId(), resCommentDto);

            // 부모 댓글이 있으면
            if (comment.getParentComment() != null) {
                map.get(comment.getParentComment().getId()).getChildren().add(resCommentDto);
            } else {
                resCommentDtos.add(resCommentDto);
            }
        });
        return resCommentDtos;
    }

    public List<ResCommentDto> updateComment(long postId, long commentId, ReqUpdateCommentDto reqUpdateCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));
        User user = userRepository.findById(reqUpdateCommentDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글 미존재"));

        if (!comment.getPost().isSamePost(post.getId())) {
            throw new RuntimeException("게시글 불일치");
        }

        if (!comment.getUser().isWriter(user.getId())) {
            throw new RuntimeException("작성자 불일치");
        }

        comment.updateBody(reqUpdateCommentDto.getBody());

        return getComments(postId);
    }

    public void deleteComment(long postId, long commentId, ReqDeleteCommentDto reqDeleteCommentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));
        User user = userRepository.findById(reqDeleteCommentDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("댓글 미존재"));

        if (!comment.getPost().isSamePost(post.getId())) {
            throw new RuntimeException("게시글 불일치");
        }

        if (!comment.getUser().isWriter(user.getId())) {
            throw new RuntimeException("작성자 불일치");
        }

        int childrenCommentCount = comment.getComments().size();
        if (childrenCommentCount > 0) {
            throw new RuntimeException("대댓글이 있는 경우 삭제 불가");
        }

        commentRepository.deleteById(commentId);
    }
}
