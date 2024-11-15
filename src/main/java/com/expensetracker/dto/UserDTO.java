package com.expensetracker.dto;

public class UserDTO {
    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean isActive = true;


    public UserDTO(String username, String email, String firstName, String lastName, boolean isActive) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
    }

    public UserDTO() {};


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return String.format("username='%s', email='%s', firstName='%s', lastName='%s'}",
                username, email, firstName, lastName);
    }
}
