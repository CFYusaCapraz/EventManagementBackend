package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.RefreshToken;
import com.yusacapraz.auth.model.User;
import com.yusacapraz.auth.model.exception.RefreshTokenExpiredException;
import com.yusacapraz.auth.model.exception.RefreshTokenNotFoundException;
import com.yusacapraz.auth.repository.RefreshTokenRepository;
import com.yusacapraz.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String username) {
        Optional<User> userOptional = userRepository.findUserByUsernameAndIsDeletedIsFalse(username);
        if (userOptional.isPresent()) {
            RefreshToken refreshToken = RefreshToken.builder()
                    .refreshToken(UUID.randomUUID())
                    .expiryDate(LocalDateTime.now().plusHours(1)) // 1 Hour refresh token expiry time
                    .user(userOptional.get())
                    .build();
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        throw new UsernameNotFoundException("User of the given username not found!");
    }

    public RefreshToken findRefreshTokenByToken(UUID refreshToken) {
        return refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Refresh token of given token not found"));
    }

    public void isRefreshTokenExpired(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenExpiredException("Refresh token of the given token is expired at %s ! Please login again!"
                    .formatted(refreshToken.getExpiryDate().toString()));
        }
    }

}
