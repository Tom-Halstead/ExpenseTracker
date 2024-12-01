package com.expensetracker.exception;

public class UserConfirmationRequiredException extends RuntimeException {
    public UserConfirmationRequiredException(String message) {
        super(message);
    }
}

