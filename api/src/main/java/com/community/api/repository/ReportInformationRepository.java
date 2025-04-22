package com.community.api.repository;

import com.community.api.model.ReportInformation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportInformationRepository extends JpaRepository<ReportInformation, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM ReportInformation r WHERE r.id IN :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM ReportInformation r WHERE r.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);

    @Query("SELECT r FROM ReportInformation r WHERE r.postId = :postId")
    Optional<ReportInformation> findByPostId(@Param("postId") Long postId);

}
