package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.exceptions.InvalidExpenseDataException;
import com.expensetracker.service.ExpenseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<ExpenseDTO> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getExpense(@PathVariable int id) {
        try {
            ExpenseDTO expense = expenseService.getExpenseById(id);
            return ResponseEntity.ok(expense);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Expense not found
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO) {
        try {
            ExpenseDTO createdExpense = expenseService.addExpense(expenseDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (InvalidExpenseDataException e) { // custom exception for validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Invalid expense data
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // General server error
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable int id, @RequestBody ExpenseDTO expenseDTO) {
        try {
            ExpenseDTO updatedExpense = expenseService.updateExpense(id, expenseDTO);
            return ResponseEntity.ok(updatedExpense);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Expense not found
        } catch (InvalidExpenseDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // Invalid expense data
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable int id) {
        try {
            expenseService.deleteExpenseById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Expense not found
        }
    }

    // Custom exception handlers for better control
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(InvalidExpenseDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidExpenseDataException(InvalidExpenseDataException e) {
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception e) {
        return "An unexpected error occurred. Please try again later.";
    }
}
