package com.expensetracker.dto;

import java.time.LocalDateTime;

public class CategoryDTO {

    private int categoryId;
    private int userId; // Non-null to align with database structure
    private String name;
    private String type;
    private String description;
    private boolean isActive = true; // Default to true
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // Added updatedAt for consistency

    // Default constructor
    public CategoryDTO() {}

    // Constructor for new records (without ID or timestamps)
    public CategoryDTO(int userId, String name, String type, String description, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActive = isActive;
    }

    // Constructor with all fields for existing records
    public CategoryDTO(int categoryId, int userId, String name, String type, String description, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
        return "CategoryDTO{" +
                "categoryId=" + categoryId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
