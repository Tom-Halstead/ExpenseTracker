package com.expensetracker.model.jdbc;

import com.expensetracker.model.dao.IncomeDao;
import com.expensetracker.model.entities.Income;

import java.util.List;

public class JdbcIncomeDao implements IncomeDao {
    @Override
    public List<Income> getAllIncome() {
        return List.of();
    }

    @Override
    public Income getIncomeById(int id) {
        return null;
    }

    @Override
    public void addIncome(Income income) {

    }

    @Override
    public void updateIncome(Income income) {

    }

    @Override
    public void deleteIncome(int id) {

    }
}
