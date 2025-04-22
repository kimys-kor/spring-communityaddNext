package com.community.api.repository;

import com.community.api.model.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {


}
