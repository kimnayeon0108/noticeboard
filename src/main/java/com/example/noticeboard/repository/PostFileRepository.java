package com.example.noticeboard.repository;

import com.example.noticeboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    @Modifying
    @Query("UPDATE PostFile pf SET pf.isDeleted = true WHERE pf.post.id = :postId")
    int deleteAllByPostId(@Param("postId") long postId);
}
