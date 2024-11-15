package com.expensetracker.controller;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.exceptions.BudgetNotFoundException;
import com.expensetracker.exceptions.InvalidBudgetDataException;
import com.expensetracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Get all budgets
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getAllBudgets() {
        List<BudgetDTO> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(budgets);
    }

    // Get a single budget by ID
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudgetById(@PathVariable int id) {
        BudgetDTO budget = budgetService.getBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    // Create a new budget
    @PostMapping
    public ResponseEntity<BudgetDTO> addBudget(@RequestBody BudgetDTO budgetDTO) {
        BudgetDTO newBudget = budgetService.addBudget(budgetDTO);
        return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
    }

    // Update an existing budget
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable int id, @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.updateBudget(id, budgetDTO);
        return ResponseEntity.ok(updatedBudget);
    }

    // Delete a budget
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable int id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    // Exception Handlers
    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> handleBudgetNotFound(BudgetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidBudgetDataException.class)
    public ResponseEntity<String> handleInvalidBudgetData(InvalidBudgetDataException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}