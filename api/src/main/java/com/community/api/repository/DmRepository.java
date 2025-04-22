package com.community.api.repository;

import com.community.api.model.Dm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DmRepository extends JpaRepository<Dm, Long> {
    @Modifying
    @Query("delete from Dm d where d.id in :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

}
