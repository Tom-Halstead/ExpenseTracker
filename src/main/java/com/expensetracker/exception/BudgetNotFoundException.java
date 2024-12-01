package com.expensetracker.exception;

public class BudgetNotFoundException extends RuntimeException {

    public BudgetNotFoundException(int id) {
        super("Budget with ID " + id + " not found.");
    }
}
