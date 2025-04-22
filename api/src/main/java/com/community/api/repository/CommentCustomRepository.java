package com.community.api.repository;

import com.community.api.model.Comment;
import com.community.api.model.dto.ReadSearchCommentDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.community.api.model.QComment.comment;
@Repository
public class CommentCustomRepository {

    @PersistenceContext
    private EntityManager em;


    public Map<String, Object> findByboardId(Long boardId, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<Comment> results = queryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(boardId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Comment> comments = results.getResults();
        long total = results.getTotal();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("comments", comments);
        resultMap.put("total", total);

        return resultMap;
    }

    public Map<String, Object> searchComment(String keyword, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<ReadSearchCommentDto> results = queryFactory.select(Projections.fields(ReadSearchCommentDto.class,
                        comment.id,
                        comment.content,
                        comment.username,
                        comment.nickname,
                        comment.createdDt
                ))
                .from(comment)
                .where(
                        comment.isDeleted.isFalse(),
                        keywordFilter(keyword)

                )
                .orderBy(
                        comment.createdDt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadSearchCommentDto> comments = results.getResults();
        long total = results.getTotal();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("comments", comments);
        resultMap.put("total", total);

        return resultMap;
    }

    private BooleanExpression keywordFilter(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return comment.content.contains(keyword).or(comment.username.contains(keyword)).or(comment.nickname.contains(keyword));
    }
}
