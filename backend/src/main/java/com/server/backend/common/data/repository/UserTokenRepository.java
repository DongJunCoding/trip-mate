package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.UserTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserTokenEntity, Long> {

    Boolean existsByRefreshToken(String refresh);

    void deleteByRefreshToken(String refresh);

    void deleteByUsername(String username);
}
