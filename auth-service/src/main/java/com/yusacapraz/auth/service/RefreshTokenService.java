package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.RefreshToken;
import com.yusacapraz.auth.model.User;
import com.yusacapraz.auth.model.exception.RefreshTokenExpiredException;
import com.yusacapraz.auth.model.exception.RefreshTokenNotFoundException;
import com.yusacapraz.auth.model.exception.UserAlreadyLoggedInException;
import com.yusacapraz.auth.model.exception.UserAlreadyLoggedOutException;
import com.yusacapraz.auth.repository.RefreshTokenRepository;
import com.yusacapraz.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public RefreshToken createRefreshToken(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsernameAndIsDeletedIsFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User of the given username not found!"));
        refreshTokenRepository.findRefreshTokenByUser(user)
                .ifPresent(loggedInUser -> {
                    throw new UserAlreadyLoggedInException("User is already logged in. Use refresh token to get a new access token!");
                });
        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(UUID.randomUUID())
                .expiryDate(LocalDateTime.now().plusHours(1)) // 1 Hour refresh token expiry time
                .user(user)
                .build();
        refreshTokenRepository.saveAndFlush(refreshToken);
        return refreshToken;
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

    public void logout(UUID uuid) throws UserAlreadyLoggedOutException {
        refreshTokenRepository.findRefreshTokenByRefreshToken(uuid)
                .ifPresentOrElse(refreshTokenRepository::delete,
                        () -> {
                            throw new UserAlreadyLoggedOutException("Refresh token of the user is already logged out!");
                        });
    }
}
