package com.expensetracker.service;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ExpenseNotFoundException;
import com.expensetracker.exception.CategoryNotFoundException;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ExpenseDTO> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        return expenses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseDTO getExpenseById(int id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));
        return convertToDTO(expense);
    }

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        Expense expense = convertToEntity(expenseDTO);
        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
    }

    public ExpenseDTO updateExpense(int id, ExpenseDTO expenseDTO) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));  // Adjusted exception for consistency
        updateEntityWithDTO(expense, expenseDTO);
        expense = expenseRepository.save(expense);
        return convertToDTO(expense);
    }

    public void deleteExpenseById(int id) {
        if (!expenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    private ExpenseDTO convertToDTO(Expense expense) {
        return new ExpenseDTO(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getDate(),
                expense.isRecurring(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getUser().getId(),
                expense.getCreatedAt(),
                expense.getUpdatedAt()
        );
    }

    private Expense convertToEntity(ExpenseDTO expenseDTO) {
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + expenseDTO.getCategoryId()));
        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + expenseDTO.getUserId()));

        Expense expense = new Expense();
        expense.setId(expenseDTO.getId());
        expense.setDescription(expenseDTO.getDescription());
        expense.setAmount(expenseDTO.getAmount());
        expense.setDate(expenseDTO.getDate());
        expense.setRecurring(expenseDTO.isRecurring());
        expense.setCategory(category);
        expense.setUser(user);

        return expense;
    }


    private void updateEntityWithDTO(Expense expense, ExpenseDTO expenseDTO) {
        if (expenseDTO.getAmount() != null) {
            expense.setAmount(expenseDTO.getAmount());
        }
        if (expenseDTO.getDescription() != null) {
            expense.setDescription(expenseDTO.getDescription());
        }
        if (expenseDTO.getDate() != null) {
            expense.setDate(expenseDTO.getDate());
        }
        if (expenseDTO.isRecurring()) {
            expense.setRecurring(expenseDTO.isRecurring());
        }
    }
}
