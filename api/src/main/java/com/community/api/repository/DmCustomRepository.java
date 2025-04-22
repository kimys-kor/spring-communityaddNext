package com.community.api.repository;

import com.community.api.model.dto.ReadDmListDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.community.api.model.QDm.dm;

@Repository
public class DmCustomRepository {

    @PersistenceContext
    private EntityManager em;

    public Page<ReadDmListDto> getList(String username, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);


        QueryResults<ReadDmListDto> results = queryFactory.select(Projections.fields(ReadDmListDto.class,
                        dm.id,
                        dm.sender,
                        dm.title,
                        dm.isChecked,
                        dm.createdDt
                ))
                .from(dm)
                .where(
                        dm.receiver.eq(username)
                )
                .orderBy(
                        dm.createdDt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        // 결과를 Pageable 형태로 변환
        List<ReadDmListDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

}
