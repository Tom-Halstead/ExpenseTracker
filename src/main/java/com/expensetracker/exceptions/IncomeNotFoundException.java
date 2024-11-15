package com.expensetracker.exceptions;

public class IncomeNotFoundException extends RuntimeException {
    public IncomeNotFoundException(String message) {
        super(message);
    }
}
