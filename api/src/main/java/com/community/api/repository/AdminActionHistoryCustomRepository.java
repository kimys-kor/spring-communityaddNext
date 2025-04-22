package com.community.api.repository;

import com.community.api.model.dto.AdminActionHistoryDto;
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

import static com.community.api.model.QAdminActionHistory.adminActionHistory;

@Repository
public class AdminActionHistoryCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<AdminActionHistoryDto> findAll(String keyword, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<AdminActionHistoryDto> data = queryFactory
                .select(Projections.fields(
                        AdminActionHistoryDto.class,
                        adminActionHistory.id,
                        adminActionHistory.actionType,
                        adminActionHistory.username,
                        adminActionHistory.content,
                        adminActionHistory.createdDt
                ))
                .from(adminActionHistory)
                .where(
                        keywordFilter(keyword)
                )
                .orderBy(
                        adminActionHistory.createdDt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(adminActionHistory.count())
                .from(adminActionHistory)
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
        return adminActionHistory.username.contains(keyword).or(adminActionHistory.content.contains(keyword));
    }
}