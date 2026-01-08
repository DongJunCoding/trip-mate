package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.UserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

    Boolean existsByRefreshToken(String refresh);

    void deleteByRefreshToken(String refresh);

    void deleteByUserId(String username);

    void deleteByCreatedDateBefore(LocalDateTime createdDate);
}
