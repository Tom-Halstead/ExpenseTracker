package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IncomeDTO {
    private int id;
    private int userId;
    private int categoryId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String source;


    public IncomeDTO() {}
    // Constructor for new records without an ID, including user and category IDs
    public IncomeDTO(int userId, int categoryId, BigDecimal amount, LocalDate date, String description, String source) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.source = source;
    }

    // Constructor for existing records with an ID
    public IncomeDTO(int id, int userId, int categoryId, BigDecimal amount, LocalDate date, String description, String source) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.source = source;
    }

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
