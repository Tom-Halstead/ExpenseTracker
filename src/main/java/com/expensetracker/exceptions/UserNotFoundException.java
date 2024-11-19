package com.expensetracker.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super("User with given ID not found.");
    }
}
