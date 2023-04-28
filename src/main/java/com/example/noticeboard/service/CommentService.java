package com.example.noticeboard.service;

import com.example.noticeboard.domain.Comment;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.CommentRequest;
import com.example.noticeboard.dto.CommentResponse;
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

    public List<CommentResponse> addComment(long postId, CommentRequest commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));
        User user = userRepository.findById(commentRequest.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));

        Comment comment = new Comment(post, user, commentRequest.getBody());

        // parentComment id가 있다면 조회
        if (commentRequest.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId()).orElseThrow(() -> new RuntimeException("댓글 미존재"));  //Todo: custom exception

            if (!comment.getPost().isSamePost(parentComment.getPost().getId())) {
                throw new RuntimeException("댓글의 게시글이 같아야 대댓글 작성 가능");
            }
            comment.updateParentComment(parentComment);
        }
        commentRepository.save(comment);

        return getComments(postId);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);

        List<CommentResponse> commentResponses = new ArrayList<>();
        Map<Long, CommentResponse> map = new HashMap<>();
        comments.forEach(comment -> {
            CommentResponse commentResponse = CommentResponse.of(comment);
            map.put(comment.getId(), commentResponse);

            // 부모 댓글이 있으면
            if (comment.getParentComment() != null) {
                map.get(comment.getParentComment().getId()).getChildren().add(commentResponse);
            } else {
                commentResponses.add(commentResponse);
            }
        });
        return commentResponses;
    }
}
