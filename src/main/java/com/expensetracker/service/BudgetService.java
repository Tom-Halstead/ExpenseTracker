package com.expensetracker.service;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.model.Budget;
import com.expensetracker.model.Category;
import com.expensetracker.model.User;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BudgetDTO getBudgetById(int id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        return convertToDTO(budget);
    }

    public BudgetDTO addBudget(BudgetDTO budgetDTO) {
        Budget budget = convertToEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    public BudgetDTO updateBudget(int id, BudgetDTO budgetDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        updateEntity(budget, budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    public void deleteBudget(int id) {
        budgetRepository.deleteById(id);
    }

    private BudgetDTO convertToDTO(Budget budget) {
        BudgetDTO dto = new BudgetDTO();
        dto.setId(budget.getId());
        dto.setUserId(budget.getUser().getId());
        dto.setCategoryId(budget.getCategory().getId());
        dto.setAmount(budget.getAmount());
        dto.setMonth(budget.getMonth());
        dto.setYear(budget.getYear());
        return dto;
    }

    private Budget convertToEntity(BudgetDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setAmount(dto.getAmount());
        budget.setMonth(dto.getMonth());
        budget.setYear(dto.getYear());
        return budget;
    }

    private void updateEntity(Budget budget, BudgetDTO dto) {
        budget.setAmount(dto.getAmount());
        budget.setMonth(dto.getMonth());
        budget.setYear(dto.getYear());
    }
}