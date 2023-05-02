package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int updateViewCount(long postId);

    List<Post> findAllByIdIn(List<Long> postIds);

    @Modifying
    @Query("UPDATE Post p SET p.isDeleted = true WHERE p.id IN (:postIds)")
    int deleteAllById(List<Long> postIds);
}
