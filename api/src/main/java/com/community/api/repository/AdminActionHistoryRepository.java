package com.community.api.repository;

import com.community.api.model.AdminActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminActionHistoryRepository extends JpaRepository<AdminActionHistory, Long> {




}
