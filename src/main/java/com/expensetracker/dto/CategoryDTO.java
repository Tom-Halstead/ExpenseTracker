package com.expensetracker.dto;

import java.time.LocalDateTime;

public class CategoryDTO {

    private int categoryId;
    private Integer userId;
    private String name;
    private String type;
    private String description;
    private boolean isActive;
    private LocalDateTime createdAt;

    // Default constructor
    public CategoryDTO() {}

    // Constructor without ID for new records
    public CategoryDTO(int userId, String name, String type, String description, boolean isActive) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActive = isActive;
    }

    // Constructor with ID for existing records
    public CategoryDTO(int categoryId, int userId, String name, String type, String description, boolean isActive, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
                '}';
    }
}