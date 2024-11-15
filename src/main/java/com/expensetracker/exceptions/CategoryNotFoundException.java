package com.expensetracker.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(int categoryId) {
        super("Category with ID " + categoryId + " not found.");
    }
}
