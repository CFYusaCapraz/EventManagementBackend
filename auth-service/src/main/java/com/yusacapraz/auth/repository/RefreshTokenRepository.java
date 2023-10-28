package com.yusacapraz.auth.repository;

import com.yusacapraz.auth.model.RefreshToken;
import com.yusacapraz.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByRefreshToken(UUID refreshToken);

    Optional<RefreshToken> findRefreshTokenByUser(User user);
}
