package com.community.api.repository;


import com.community.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Integer countAllByCreatedDtBetween(LocalDateTime start, LocalDateTime end);


    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = :currentDateTime WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("currentDateTime") LocalDateTime currentDateTime);
}
