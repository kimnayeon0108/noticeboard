package com.example.noticeboard.repository;

import com.example.noticeboard.domain.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {

    boolean existsByCategoryIdIn(List<Long> categoryIds);

    @Modifying
    @Query("DELETE FROM PostCategory pc WHERE pc.post.id in (:postIds)")
    void deleteAllByPostIdInBatch(@Param("postIds") List<Long> postIds);
}
