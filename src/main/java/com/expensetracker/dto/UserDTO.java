package com.expensetracker.dto;

public class UserDTO {
    private int id;
    private String cognitoUuid;  // Unique identifier from Cognito
    private String username;     // Local username for display or internal use
    private String email;        // Email used as the username in Cognito
    private String firstName;
    private String lastName;
    private boolean isActive = true;
    private transient String password; // Not stored or serialized, used transiently for registration
    private String status;

    // Constructor for creating a new user
    public UserDTO(int id, String cognitoUuid, String username, String email, String firstName, String lastName, boolean isActive, String status) {
        this.id = id;
        this.cognitoUuid = cognitoUuid;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.status = status;
    }

    /**
     * Constructor for initial user creation before ID is set.
     */
    public UserDTO(String cognitoUuid, String username, String email, String firstName, String lastName, boolean isActive, String password) {
        this.cognitoUuid = cognitoUuid;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.password = password; // Transient use for registration
    }

    /**
     * No-argument constructor for frameworks.
     */
    public UserDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }

    public String getCognitoUuid() {
        return cognitoUuid;
    }

    public void setCognitoUuid(String cognitoUuid) {
        this.cognitoUuid = cognitoUuid;
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
}



