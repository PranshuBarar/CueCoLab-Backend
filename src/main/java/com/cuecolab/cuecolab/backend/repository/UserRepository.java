package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    UserEntity findByUserId(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    UserEntity findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByEmail(String email);
}
