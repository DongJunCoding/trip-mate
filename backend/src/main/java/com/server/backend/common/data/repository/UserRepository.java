package com.server.backend.common.data.repository;

import com.server.backend.common.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByUserId(String userId);

    Optional<UserEntity> findByUserIdAndIsLock(String userId, Boolean isLock);
    Optional<UserEntity> findByUserIdAndIsSocial(String username, Boolean social);
    Optional<UserEntity> findByUserIdAndIsLockAndIsSocial(String userId, Boolean isLock, Boolean isSocial);
}
