package com.expensetracker.dto;

public class RegistrationResult {
    private String status;
    private String message;
    private String username; // Username of the newly registered user

    // Constructor
    public RegistrationResult(String status, String message, String username) {
        this.status = status;
        this.message = message;
        this.username = username;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // ToString method for logging
    @Override
    public String toString() {
        return "RegistrationResult{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
