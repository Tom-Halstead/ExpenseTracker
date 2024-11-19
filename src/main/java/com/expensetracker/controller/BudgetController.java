package com.expensetracker.controller;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.exceptions.BudgetNotFoundException;
import com.expensetracker.exceptions.InvalidBudgetDataException;
import com.expensetracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/budgets")
@Validated
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    /**
     * Retrieves all budgets.
     *
     * @return a list of all budgets as DTOs.
     */
    @GetMapping
    public ResponseEntity<List<BudgetDTO>> getAllBudgets() {
        List<BudgetDTO> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(budgets);
    }

    /**
     * Retrieves a single budget by its ID.
     *
     * @param id the ID of the budget.
     * @return the budget DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDTO> getBudgetById(@PathVariable int id) {
        BudgetDTO budget = budgetService.getBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    /**
     * Creates a new budget.
     *
     * @param budgetDTO the data of the budget to be created.
     * @return the created budget DTO.
     */
    @PostMapping
    public ResponseEntity<BudgetDTO> addBudget(@Valid @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO newBudget = budgetService.addBudget(budgetDTO);
        return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
    }

    /**
     * Updates an existing budget.
     *
     * @param id        the ID of the budget to update.
     * @param budgetDTO the updated data for the budget.
     * @return the updated budget DTO.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDTO> updateBudget(@PathVariable int id, @Valid @RequestBody BudgetDTO budgetDTO) {
        BudgetDTO updatedBudget = budgetService.updateBudget(id, budgetDTO);
        return ResponseEntity.ok(updatedBudget);
    }

    /**
     * Deletes a budget by its ID.
     *
     * @param id the ID of the budget to delete.
     * @return a response with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable int id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handles BudgetNotFoundException.
     *
     * @param ex the exception instance.
     * @return a 404 Not Found response with the error message.
     */
    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<String> handleBudgetNotFound(BudgetNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles InvalidBudgetDataException.
     *
     * @param ex the exception instance.
     * @return a 400 Bad Request response with the error message.
     */
    @ExceptionHandler(InvalidBudgetDataException.class)
    public ResponseEntity<String> handleInvalidBudgetData(InvalidBudgetDataException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
