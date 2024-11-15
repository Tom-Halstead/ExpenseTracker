package com.expensetracker.exceptions;

public class InvalidIncomeDataException extends RuntimeException {
    public InvalidIncomeDataException(String message) {
        super(message);
    }
}
