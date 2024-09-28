package com.expensetracker.model.dao;

import com.expensetracker.model.entities.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAllCategories();
    Category getCategoryById(int id);
    void addCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(int id);
}
