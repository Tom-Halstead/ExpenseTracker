package com.expensetracker.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private int categoryId;

    public Expense(int categoryId, LocalDate date, double amount, String description, int id) {
        this.categoryId = categoryId;
        this.date = date;
        this.amount = BigDecimal.valueOf(amount);
        this.description = description;
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = BigDecimal.valueOf(amount);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
