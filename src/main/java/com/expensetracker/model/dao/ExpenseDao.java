package com.expensetracker.model.dao;

import com.expensetracker.model.entities.Expense;

import java.util.List;

public interface ExpenseDao {
    List<Expense> getAllExpenses();
    Expense getExpenseById(int id);
    void addExpense(Expense expense);
    void updateExpense(Expense expense);
    void deleteExpense(int id);
}
