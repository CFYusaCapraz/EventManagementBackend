package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.User;
import com.yusacapraz.auth.repository.UserRepository;
import com.yusacapraz.auth.security.JDBCUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JDBCUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public JDBCUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByUsernameAndIsDeletedIsFalse(username);

        return user.map(JDBCUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
