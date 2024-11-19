package com.expensetracker.dto;

import java.math.BigDecimal;

public class BudgetDTO {
    private int id;
    private int userId;
    private Integer categoryId; // Changed to Integer to allow null values
    private BigDecimal amount;
    private int month;
    private int year;

    // Full constructor including ID
    public BudgetDTO(int id, int userId, Integer categoryId, BigDecimal amount, int month, int year) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    // Constructor for creating a new Budget without ID
    public BudgetDTO(int userId, Integer categoryId, BigDecimal amount, int month, int year) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    // Default constructor
    public BudgetDTO() {}

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "BudgetDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", amount=" + amount +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}
