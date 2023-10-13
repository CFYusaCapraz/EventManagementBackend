package com.yusacapraz.auth.repository;

import com.yusacapraz.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByUsernameAndIsDeletedIsFalse(String username);
    Optional<User> findUserByUserIdAndIsDeletedIsFalse(UUID userId);
}
