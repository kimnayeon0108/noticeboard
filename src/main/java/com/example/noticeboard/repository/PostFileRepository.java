package com.example.noticeboard.repository;

import com.example.noticeboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    @Modifying
    @Query("UPDATE PostFile pf SET pf.isDeleted = true WHERE pf.post.id = :postId")
    int deleteAllByPostId(long postId);
}
