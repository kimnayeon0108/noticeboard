package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Post;
import com.example.noticeboard.dto.request.ReqPostListParamsDto;
import com.example.noticeboard.type.PostOrderType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.noticeboard.domain.QPost.post;
import static com.example.noticeboard.domain.QPostCategory.postCategory;
import static com.example.noticeboard.domain.QPostFile.postFile;
import static com.example.noticeboard.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> findAllByConditions(ReqPostListParamsDto requestParams, Pageable pageable) {
        List<Post> contents = jpaQueryFactory.selectFrom(post)
                                             .join(post.user, user)
                                             .fetchJoin()
                                             .join(post.postCategories, postCategory)
                                             .leftJoin(post.postFiles, postFile)
                                             .distinct()
                                             .where(titleContains(requestParams.getTitle()),
                                                     userIdEq(requestParams.getUserId()),
                                                     bodyContains(requestParams.getBody()),
                                                     categoryIdEq(requestParams.getCategoryId()),
                                                     post.publicState.isTrue())
                                             .orderBy(getListOrderSpecifier(requestParams.getOrderBy()))
                                             .limit(pageable.getPageSize())
                                             .offset(pageable.getOffset())
                                             .fetch();

        long totalSize = jpaQueryFactory.select(post.countDistinct())
                                        .from(post)
                                        .join(post.postCategories, postCategory)
                                        .where(titleContains(requestParams.getTitle()),
                                                userIdEq(requestParams.getUserId()),
                                                bodyContains(requestParams.getBody()),
                                                categoryIdEq(requestParams.getCategoryId()))
                                        .fetchOne();

        return new PageImpl<>(contents, pageable, totalSize);
    }

    private BooleanExpression titleContains(String title) {
        return !StringUtils.isBlank(title) ? post.title.contains(title) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? user.id.eq(userId) : null;
    }

    private BooleanExpression bodyContains(String body) {
        return !StringUtils.isBlank(body) ? post.body.contains(body) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? postCategory.category.id.eq(categoryId) : null;
    }

    private OrderSpecifier[] getListOrderSpecifier(PostOrderType postOrder) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        switch (postOrder) {
            case CREATED_AT:
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, post.createdAt));
                break;
            case UPDATED_AT:
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, post.updatedAt));
                break;
            case VIEW_COUNT:
                orderSpecifiers.add(new OrderSpecifier(Order.DESC, post.viewCount));
                break;
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
