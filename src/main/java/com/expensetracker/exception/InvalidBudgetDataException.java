package com.expensetracker.exception;

public class InvalidBudgetDataException extends RuntimeException {

    public InvalidBudgetDataException(String message) {
        super(message);
    }
}
