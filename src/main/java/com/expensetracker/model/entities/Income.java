package com.expensetracker.model.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income {
    private int id;
    private int categoryId;
    private int userId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String source;

    public Income(int id, int categoryId, int userId, BigDecimal amount, LocalDate date, String description, String source) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
