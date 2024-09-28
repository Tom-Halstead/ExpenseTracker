package com.expensetracker.model.jdbc;

import com.expensetracker.model.dao.ExpenseDao;
import com.expensetracker.model.entities.Expense;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcExpenseDao implements ExpenseDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcExpenseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Expense> getAllExpenses() {
        String sql = "SELECT * FROM expenses";
        return jdbcTemplate.query(sql, new ExpenseRowMapper());
    }

    @Override
    public Expense getExpenseById(int id) {
        String sql = "SELECT * FROM expenses WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new ExpenseRowMapper(), id);
    }

    @Override
    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (description, amount, date, category_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategoryId());
    }

    @Override
    public void updateExpense(Expense expense) {
        String sql = "UPDATE expenses SET description = ?, amount = ?, date = ?, category_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategoryId(), expense.getId());
    }

    @Override
    public void deleteExpense(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
