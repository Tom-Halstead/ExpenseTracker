package com.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ExpenseDTO {
    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private boolean recurring;
    private int categoryId;
    private String categoryName;
    private LocalDateTime now;
    private int userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ExpenseDTO() {}

    public ExpenseDTO(int userId, int categoryId, BigDecimal amount, LocalDateTime date, String description, String source, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public ExpenseDTO(int id, String description, BigDecimal amount, LocalDateTime date, boolean recurring, int categoryId, String categoryName, int userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.recurring = recurring;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ExpenseDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", recurring=" + recurring +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
