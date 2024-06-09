package com.cuecolab.cuecolab.backend.repository;

import com.cuecolab.cuecolab.backend.entities.TokenEntity;
import com.cuecolab.cuecolab.backend.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    List<TokenEntity> findByUserEntityAndLoggedOutFalse(UserEntity userEntity);
    Optional<TokenEntity> findByToken(String token);
}
