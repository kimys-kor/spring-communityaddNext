package com.community.api.repository;

import com.community.api.model.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, String> {

    boolean existsByRefreshTokenAndUsername(String refreshToken, String email);

    @Transactional
    void deleteByUsernameEquals(@Param("email") String email);
}
