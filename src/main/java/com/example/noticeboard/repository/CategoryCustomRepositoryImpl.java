package com.example.noticeboard.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.noticeboard.domain.QCategory.category;
import static com.example.noticeboard.domain.QPostCategory.postCategory;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public long findLastDepthCategoryIdByPostId(long postId) {
        return jpaQueryFactory.select(category.id)
                              .from(category)
                              .join(category.postCategories, postCategory)
                              .where(postCategory.post.id.eq(postId))
                              .orderBy(category.depth.desc())
                              .fetchFirst();
    }
}
