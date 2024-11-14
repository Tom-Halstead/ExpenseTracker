package com.expensetracker.controller;

import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // Get all incomes
    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllIncomes();
        return ResponseEntity.ok(incomes);
    }

    // Get a single income by ID
    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getIncomeById(@PathVariable int id) {
        IncomeDTO income = incomeService.getIncomeById(id);
        return ResponseEntity.ok(income);
    }

    // Create a new income
    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO newIncome = incomeService.addIncome(incomeDTO);
        return new ResponseEntity<>(newIncome, HttpStatus.CREATED);
    }

    // Update an existing income
    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> updateIncome(@PathVariable int id, @RequestBody IncomeDTO incomeDTO) {
        IncomeDTO updatedIncome = incomeService.updateIncome(id, incomeDTO);
        return ResponseEntity.ok(updatedIncome);
    }

    // Delete an income
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable int id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}
