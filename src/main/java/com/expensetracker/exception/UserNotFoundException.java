package com.expensetracker.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super("No active user with given ID not found.");
    }
}
