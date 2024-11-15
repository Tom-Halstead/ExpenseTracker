package com.expensetracker.exceptions;

public class InvalidBudgetDataException extends RuntimeException {

    public InvalidBudgetDataException(String message) {
        super(message);
    }
}
