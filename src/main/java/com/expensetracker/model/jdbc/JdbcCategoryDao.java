package com.expensetracker.model.jdbc;

import com.expensetracker.model.dao.CategoryDao;
import com.expensetracker.model.entities.Category;

import java.util.List;

public class JdbcCategoryDao implements CategoryDao {
    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }

    @Override
    public Category getCategoryById(int id) {
        return null;
    }

    @Override
    public void addCategory(Category category) {

    }

    @Override
    public void updateCategory(Category category) {

    }

    @Override
    public void deleteCategory(int id) {

    }
}
