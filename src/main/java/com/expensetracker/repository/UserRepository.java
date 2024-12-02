package com.expensetracker.repository;

import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int userId);

    User findByUsername(String username);

    void deleteByUsername(String username);

    Optional<User> findByCognitoUuid(String cognitoUuid);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<User> findUsernameByEmail(String email);
    Optional<User> findByEmail(String email);
}
