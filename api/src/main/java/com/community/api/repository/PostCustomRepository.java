package com.community.api.repository;


import com.community.api.model.dto.*;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.community.api.model.QPost.post;
import static com.community.api.model.QReportInformation.reportInformation;


@Repository
public class PostCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<ReadPostListDto> getList(int typ, String keyword, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        QueryResults<ReadPostListDto> results = queryFactory.select(Projections.fields(ReadPostListDto.class,
                        post.id,
                        post.postType,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.hit,
                        post.hate,
                        post.thumbNail,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                        ))
                .from(post)
                .where(
                        post.postType.eq(typ),
                        post.isDeleted.eq(false),
                        keywordFilter(keyword)
                )
                .orderBy(post.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadPostListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<ReadReportListDto> getReportList(int typ, String keyword, Integer reportTyp, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        // QueryDSL을 사용한 JPQL
        QueryResults<ReadReportListDto> results = queryFactory.select(Projections.fields(ReadReportListDto.class,
                        post.id,
                        post.postType,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.thumbNail,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt,
                        reportInformation.reportTyp,
                        reportInformation.siteName,
                        reportInformation.siteUrl,
                        reportInformation.date,
                        reportInformation.amount,
                        reportInformation.accountNumber
                ))
                .from(post)
                .leftJoin(reportInformation).on(reportInformation.postId.eq(post.id))
                .where(
                        post.postType.eq(typ),
                        post.isDeleted.eq(false),
                        keywordFilter(keyword),
                        reportTypFilter(reportTyp)
                )
                .orderBy(post.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadReportListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    public Page<ReadBestPostListDto> getBetweenList(List<Integer> typeList, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<ReadBestPostListDto> results = queryFactory.select(
                        Projections.fields(ReadBestPostListDto.class,
                                post.id,
                                post.postType,
                                post.username,
                                post.nickname,
                                post.userIp,
                                post.title,
                                post.hit,
                                post.hate,
                                post.likes,
                                post.replyNum,
                                post.createdDt
                        ))
                .from(post)
                .where(
                        post.isDeleted.eq(false)
                                .and(post.postType.in(typeList))
                )
                .orderBy(post.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadBestPostListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<ReadBestPostListDto> getBestList(String period, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        LocalDateTime timeThreshold;

        // Set the time threshold based on the period
        if ("week".equalsIgnoreCase(period)) {
            timeThreshold = LocalDateTime.now().minusDays(7);
        } else if ("day".equalsIgnoreCase(period)) {
            timeThreshold = LocalDateTime.now().minusHours(72);
        } else if ("month".equalsIgnoreCase(period)) {
            timeThreshold = LocalDateTime.now().minusMonths(1);  // Adding "month" period logic
        } else {
            throw new IllegalArgumentException("Invalid period. Allowed values are 'week', 'day', or 'month'.");
        }

        QueryResults<ReadBestPostListDto> results = queryFactory.select(Projections.fields(ReadBestPostListDto.class,
                        post.id,
                        post.postType,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                ))
                .from(post)
                .where(
                        post.isDeleted.eq(false)
                                .and(post.createdDt.after(timeThreshold))
                                .and(post.postType.between(2, 10))
                )
                .orderBy(post.hit.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadBestPostListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<ReadBestPostListDto> getNewList(Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<ReadBestPostListDto> results = queryFactory.select(Projections.fields(ReadBestPostListDto.class,
                        post.id,
                        post.postType,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                ))
                .from(post)
                .where(
                        post.isDeleted.eq(false)
                                .and(post.postType.between(6, 10))
                )
                .orderBy(post.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadBestPostListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<ReadPartnerPostListDto> getPartnerList(String keyword, int postType, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<ReadPartnerPostListDto> results = queryFactory.select(Projections.fields(ReadPartnerPostListDto.class,
                        post.id,
                        post.postType,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.thumbNail,
                        post.title,
                        post.code,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                ))
                .from(post)
                .where(
                        post.isDeleted.eq(false),
                        post.postType.eq(postType),
                        keywordFilter(keyword)
                )
                .orderBy(post.createdDt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ReadPartnerPostListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    public ReadPostContentDto getContent(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        ReadPostContentDto readPostContentDto = queryFactory.select(Projections.fields(ReadPostContentDto.class,
                        post.id,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                ))
                .from(post)
                .where(
                        post.id.eq(id),
                        post.isDeleted.eq(false)
                )
                .fetchOne();

        return readPostContentDto;
    }

    public List<ReadPostListDto> getNoticeList(int typ2) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        QueryResults<ReadPostListDto> results = queryFactory.select(Projections.fields(ReadPostListDto.class,
                        post.id,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt
                ))
                .from(post)
                .where(
                        post.postType.eq(typ2),
                        post.isDeleted.eq(false)
                )
                .orderBy(
                        post.createdDt.desc()
                )
                .fetchResults();

        List<ReadPostListDto> content = results.getResults();
        return content;
    }

    private BooleanExpression keywordFilter(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }
        return post.title.contains(keyword).or(post.content.contains(keyword)).or(post.nickname.contains(keyword));
    }

    private BooleanExpression reportTypFilter(Integer reportTyp) {
        if (reportTyp == null || reportTyp == 0) {
            return null; // reportTyp이 0 또는 null일 경우 모든 타입을 가져옴
        }
        return reportInformation.reportTyp.eq(reportTyp); // 특정 reportTyp을 필터링
    }


    public ReadReportContentDto getReportContent(Long boardId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        ReadReportContentDto readReportContentDto = queryFactory.select(Projections.fields(ReadReportContentDto.class,
                        post.id,
                        post.username,
                        post.nickname,
                        post.userIp,
                        post.title,
                        post.content,
                        post.hit,
                        post.hate,
                        post.likes,
                        post.replyNum,
                        post.createdDt,
                        reportInformation.reportTyp,
                        reportInformation.siteName,
                        reportInformation.siteUrl,
                        reportInformation.date,
                        reportInformation.amount,
                        reportInformation.accountNumber
                ))
                .from(post)
                .leftJoin(reportInformation).on(reportInformation.postId.eq(post.id))
                .where(
                        post.id.eq(boardId),
                        post.isDeleted.eq(false)
                )
                .fetchOne();

        return readReportContentDto;
    }

}
