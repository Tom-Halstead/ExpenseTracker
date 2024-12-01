package com.expensetracker.exception;

public class InvalidIncomeDataException extends RuntimeException {
    public InvalidIncomeDataException(String message) {
        super(message);
    }
}
