package com.expensetracker.repository;

import com.expensetracker.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(int userId);
}
