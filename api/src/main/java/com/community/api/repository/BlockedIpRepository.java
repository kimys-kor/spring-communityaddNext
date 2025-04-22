package com.community.api.repository;

import com.community.api.model.ApprovedIp;
import com.community.api.model.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedIpRepository extends JpaRepository<BlockedIp, Long> {


    Optional<BlockedIp> findByIpAddressEquals(String ipAddress);
}
