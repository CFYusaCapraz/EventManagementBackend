package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.RefreshToken;
import com.yusacapraz.auth.model.User;
import com.yusacapraz.auth.model.exception.UserNotFoundException;
import com.yusacapraz.auth.repository.RefreshTokenRepository;
import com.yusacapraz.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
                    .expiryDate(Instant.now().plusMillis(3600000)) // 1 Hour refresh token expiry time
                    .user(userOptional.get())
                    .build();
            refreshTokenRepository.save(refreshToken);
            return refreshToken;
        }
        throw new UsernameNotFoundException("User of the given username not found!");
    }
}
