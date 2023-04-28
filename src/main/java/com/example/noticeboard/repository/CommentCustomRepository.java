package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Comment;

import java.util.List;

public interface CommentCustomRepository {

    List<Comment> findAllByPostId(long postId);
}
