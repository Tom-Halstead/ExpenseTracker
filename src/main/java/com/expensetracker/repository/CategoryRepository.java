package com.expensetracker.repository;

import com.expensetracker.model.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(int categoryId);
}
