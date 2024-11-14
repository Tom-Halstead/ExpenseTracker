package com.expensetracker.repository;

import com.expensetracker.model.Category;
import com.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findById(int userId);

    Optional<User> findByUsername(String username);
}
