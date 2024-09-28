package com.expensetracker.model.jdbc;

import com.expensetracker.model.dao.BudgetDao;
import com.expensetracker.model.entities.Budget;

import java.util.List;

public class JdbcBudgetDao implements BudgetDao {
    @Override
    public List<Budget> getAllBudgets() {
        return List.of();
    }

    @Override
    public Budget getBudgetById(int id) {
        return null;
    }

    @Override
    public void addBudget(Budget budget) {

    }

    @Override
    public void updateBudget(Budget budget) {

    }

    @Override
    public void deleteBudget(int id) {

    }
}
