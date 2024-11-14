package com.expensetracker.repository;

import com.expensetracker.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    List<Expense> findAll();

    Expense save(Expense expense);

    Optional<Expense> findById(int id);

    void deleteById(int id);
}
