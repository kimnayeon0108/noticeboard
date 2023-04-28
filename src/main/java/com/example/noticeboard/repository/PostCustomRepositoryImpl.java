package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Post;
import com.example.noticeboard.dto.PostListRequest;
import com.example.noticeboard.type.PostOrderType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.example.noticeboard.domain.QCategory.category;
import static com.example.noticeboard.domain.QPost.post;
import static com.example.noticeboard.domain.QPostFile.postFile;
import static com.example.noticeboard.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> findAllByConditions(PostListRequest requestParams, Pageable pageable) {
        List<Post> contents = jpaQueryFactory.selectFrom(post)
                .join(post.user, user)
                .fetchJoin()
                .join(post.category, category)
                .fetchJoin()
                .leftJoin(post.postFiles, postFile)
                .distinct()
                .where(titleContains(requestParams.getTitle()),
                        userNameEq(requestParams.getWriter()),
                        bodyContains(requestParams.getBody()),
                        categoryNameEq(requestParams.getCategoryName()),
                        post.isDeleted.isFalse(),
                        post.publicState.isTrue())
                .orderBy(getListOrderSpecifier(requestParams.getPostOrder()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        long totalSize = jpaQueryFactory.select(post.count())
                .from(post)
                .where(titleContains(requestParams.getTitle()),
                        userNameEq(requestParams.getWriter()),
                        bodyContains(requestParams.getBody()),
                        categoryNameEq(requestParams.getCategoryName()))
                .fetchOne();

        return new PageImpl<>(contents, pageable, totalSize);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? post.title.contains(title) : null;
    }

    private BooleanExpression userNameEq(String userName) {
        return userName != null ? user.name.eq(userName) : null;
    }

    private BooleanExpression bodyContains(String body) {
        return body != null ? post.body.contains(body) : null;
    }

    private BooleanExpression categoryNameEq(String categoryName) {
        return categoryName != null ? category.name.eq(categoryName) : null;
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