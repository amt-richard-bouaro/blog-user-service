package com.amalitech.usermanagementservice.repository;


import com.amalitech.usermanagementservice.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);

    Optional<AuthToken> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE AuthToken a SET a.token = :blacklisted WHERE a.token = :token")
    void updateTokenBlacklisted(@Param("token") String token, @Param("blacklisted") boolean blacklisted);

    void deleteByToken(String accessToken);
    void deleteByUserId(Long userId);
    
}
