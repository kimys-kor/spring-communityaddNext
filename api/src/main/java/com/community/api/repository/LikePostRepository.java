package com.community.api.repository;

import com.community.api.model.LikePost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {

    @Query(value = "SELECT * FROM like_post l WHERE l.username = :username AND l.post_id = :postId", nativeQuery = true)
    Optional<LikePost> findByUsernameAndPostIdEquals(@Param("username") String username, @Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM like_post l where l.username = :username AND l.post_id = :postId", nativeQuery = true)
    void deleteByUsernameAndPostIdEquals(@Param("username") String username, @Param("postId") Long postId);
}
