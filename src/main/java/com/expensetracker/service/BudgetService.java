package com.expensetracker.service;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.entity.Budget;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.BudgetNotFoundException;
import com.expensetracker.exceptions.CategoryNotFoundException;
import com.expensetracker.exceptions.UserNotFoundException;
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
                .orElseThrow(() -> new BudgetNotFoundException(id));
        return convertToDTO(budget);
    }

    public BudgetDTO addBudget(BudgetDTO budgetDTO) {
        Budget budget = convertToEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    public BudgetDTO updateBudget(int id, BudgetDTO budgetDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        updateEntity(budget, budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    public void deleteBudget(int id) {
        if (!budgetRepository.existsById(id)) {
            throw new BudgetNotFoundException(id);
        }
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
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));

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