package com.example.noticeboard.repository;

import com.example.noticeboard.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.noticeboard.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findAllByPostIdOrderByDepth(long postId) {
        return jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.parentComment)
                .fetchJoin()
                .where(comment.post.id.eq(postId))
                .orderBy(comment.parentComment.id.asc().nullsFirst(),       // parentComment.id 로 정렬하면 depth로 정렬됨
                        comment.createdAt.asc())
                .fetch();
    }
}
