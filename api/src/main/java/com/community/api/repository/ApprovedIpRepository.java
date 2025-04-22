package com.community.api.repository;

import com.community.api.model.ApprovedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApprovedIpRepository extends JpaRepository<ApprovedIp, Long> {


    Optional<ApprovedIp> findByIpAddressEquals(String ipAddress);


}
