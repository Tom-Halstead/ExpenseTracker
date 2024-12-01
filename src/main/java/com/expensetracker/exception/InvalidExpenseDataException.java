package com.expensetracker.exception;

public class InvalidExpenseDataException extends RuntimeException {

    public InvalidExpenseDataException(String message) {
        super(message);
    }
}