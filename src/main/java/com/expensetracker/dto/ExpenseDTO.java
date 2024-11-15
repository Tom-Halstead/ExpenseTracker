package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String categoryName;
    private int categoryId;
    private int userId;

    public ExpenseDTO(BigDecimal amount, String description, LocalDate date) {
        this(-1, amount, description, date); // default id when not specified
    }

    // Constructor with id
    public ExpenseDTO(int id, BigDecimal amount, String description, LocalDate date) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public ExpenseDTO(){};


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
