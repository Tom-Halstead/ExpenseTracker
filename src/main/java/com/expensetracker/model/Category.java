package com.expensetracker.model;

public class Category {
    private int id;
    private String name;
    private String description;
    private String type;
    private boolean isActive;
    private int userId;


    public Category(int id, String name, String description, String type, boolean isActive, int userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.isActive = isActive;
        this.userId = userId;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
