package com.expensetracker.exceptions;

public class InvalidExpenseDataException extends RuntimeException {

    public InvalidExpenseDataException(String message) {
        super(message);
    }
}