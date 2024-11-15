package com.expensetracker.controller;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ExpenseDTO getExpense(@PathVariable int id) {
        return expenseService.getExpenseById(id);
    }

    @PostMapping
    public ExpenseDTO addExpense(@RequestBody ExpenseDTO expenseDTO) {
        return expenseService.addExpense(expenseDTO);
    }

    @PutMapping("/{id}")
    public ExpenseDTO updateExpense(@PathVariable int id, @RequestBody ExpenseDTO expenseDTO) {
        return expenseService.updateExpense(id, expenseDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable int id) {
        expenseService.deleteExpense(id);
    }
}
