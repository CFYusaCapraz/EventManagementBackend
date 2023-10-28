package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.User;
import com.yusacapraz.auth.repository.UserRepository;
import com.yusacapraz.auth.security.JDBCUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUserId(UUID userId) {
        Optional<User> userOptional = userRepository.findUserByUserIdAndIsDeletedIsFalse(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            return new JDBCUserDetails(user);

        }

        throw new UsernameNotFoundException("User not found with userId: " + userId);
    }
}
