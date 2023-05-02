package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {

    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true WHERE c.post.id = :postId")
    int deleteAllByPostId(long postId);
}
