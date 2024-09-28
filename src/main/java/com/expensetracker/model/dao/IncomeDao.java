package com.expensetracker.model.dao;

import com.expensetracker.model.entities.Income;

import java.util.List;

public interface IncomeDao {
    List<Income> getAllIncome();
    Income getIncomeById(int id);
    void addIncome(Income income);
    void updateIncome(Income income);
    void deleteIncome(int id);
}
