package com.example.noticeboard.repository;

public interface CategoryCustomRepository {

    long findLastDepthCategoryIdByPostId(long postId);

    boolean existsByParentIdAndName(Long parentCategoryId, String name);
}
