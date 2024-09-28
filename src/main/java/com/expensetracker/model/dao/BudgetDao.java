package com.expensetracker.model.dao;

import com.expensetracker.model.entities.Budget;

import java.util.List;

public interface BudgetDao {
    List<Budget> getAllBudgets();
    Budget getBudgetById(int id);
    void addBudget(Budget budget);
    void updateBudget(Budget budget);
    void deleteBudget(int id);
}
