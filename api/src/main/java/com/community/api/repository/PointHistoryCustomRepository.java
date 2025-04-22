package com.community.api.repository;

import com.community.api.model.dto.PointHistoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.community.api.model.QPointHistory.pointHistory;

@Repository
public class PointHistoryCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<PointHistoryDto> findAll(String keyword, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<PointHistoryDto> data = queryFactory
                .select(Projections.fields(
                        PointHistoryDto.class,
                        pointHistory.id,
                        pointHistory.postId,
                        pointHistory.username,
                        pointHistory.pointContent,
                        pointHistory.point,
                        pointHistory.createdDt
                ))
                .from(pointHistory)
                .where(
                        keywordFilter(keyword)
                )
                .orderBy(
                        pointHistory.createdDt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(pointHistory.count())
                .from(pointHistory)
                .where(
                        keywordFilter(keyword)
                );

        Long total = countQuery.fetchOne();

        return new PageImpl<>(data, pageable, total != null ? total : 0L);
    }

    private BooleanExpression keywordFilter(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return pointHistory.username.contains(keyword).or(pointHistory.pointContent.contains(keyword));
    }
}