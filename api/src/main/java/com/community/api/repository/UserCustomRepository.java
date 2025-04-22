package com.community.api.repository;


import com.community.api.model.QUser;
import com.community.api.model.base.UserRole;
import com.community.api.model.base.UserStatus;
import com.community.api.model.dto.UserDetailDto;
import com.community.api.model.dto.UserReadDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
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
import static com.community.api.model.QUser.user;



@Repository
public class UserCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<UserReadDto> findAll(String keyword, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        StringExpression cratedDt = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , user.createdDt
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("createdDt");

        StringExpression lastLogin = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , user.lastLogin
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("lastLogin");


        QueryResults<UserReadDto> results = queryFactory.select(Projections.fields(UserReadDto.class,
                        user.id,
                        user.username,
                        user.phoneNum,
                        user.fullName,
                        user.nickname,
                        user.point,
                        user.exp,
                        user.status,
                        cratedDt,
                        lastLogin
                ))
                .from(user)
                .where(
                        user.role.eq(UserRole.ROLE_USER),
                        keywordFilter(keyword)
                )
                .orderBy(
                        user.createdDt.desc(),
                        user.status.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<UserReadDto> data = results.getResults();


        long total = results.getTotal();
        return new PageImpl<>(data, pageable, total);

    }

    public Page<UserReadDto> findAllAdmin(String keyword, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        StringExpression cratedDt = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , user.createdDt
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("createdDt");

        StringExpression lastLogin = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , user.lastLogin
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("lastLogin");


        QueryResults<UserReadDto> results = queryFactory.select(Projections.fields(UserReadDto.class,
                        user.id,
                        user.username,
                        user.phoneNum,
                        user.fullName,
                        user.nickname,
                        user.point,
                        user.exp,
                        user.status,
                        cratedDt,
                        lastLogin
                ))
                .from(user)
                .where(
                        user.role.eq(UserRole.ROLE_ADMIN),
                        keywordFilter(keyword)
                )
                .orderBy(
                        user.createdDt.desc(),
                        user.status.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<UserReadDto> data = results.getResults();


        long total = results.getTotal();
        return new PageImpl<>(data, pageable, total);

    }

    private BooleanExpression keywordFilter(String keyword) {
        if (StringUtils.isEmpty(keyword)) {
            return null;
        }

        BooleanExpression condition = user.username.containsIgnoreCase(keyword)
                .or(user.phoneNum.containsIgnoreCase(keyword))
                .or(user.fullName.containsIgnoreCase(keyword))
                .or(user.nickname.containsIgnoreCase(keyword));

        try {
            UserStatus status = UserStatus.valueOf(keyword.toUpperCase());
            condition = condition.or(user.status.eq(status));
        } catch (IllegalArgumentException e) {
        }

        return condition;
    }

    public UserDetailDto findById(Long userId) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        StringExpression cratedDt = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , QUser.user.createdDt
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("createdDt");

        StringExpression lastLogin = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , QUser.user.lastLogin
                , ConstantImpl.create("%Y.%m.%d %H:%i:%s")).as("lastLogin");


        UserDetailDto userDetailDto = queryFactory.select(Projections.fields(UserDetailDto.class,
                        QUser.user.id,
                        QUser.user.nickname,
                        QUser.user.point,
                        QUser.user.username,
                        cratedDt,
                        lastLogin,
                        QUser.user.phoneNum
                ))
                .from(QUser.user)
                .where(
                        QUser.user.id.eq(userId),
                        QUser.user.role.eq(UserRole.ROLE_USER)
                )
                .fetchOne();

        return userDetailDto;

    }


}
