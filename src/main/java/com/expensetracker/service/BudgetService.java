package com.expensetracker.service;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.entity.Budget;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.exception.BudgetNotFoundException;
import com.expensetracker.exception.CategoryNotFoundException;
import com.expensetracker.exception.UserNotFoundException;
import com.expensetracker.repository.BudgetRepository;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Retrieves all budgets.
     *
     * @return a list of all budgets as DTOs.
     */
    public List<BudgetDTO> getAllBudgets() {
        List<Budget> budgets = budgetRepository.findAll();
        return budgets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a budget by its ID.
     *
     * @param id the budget ID.
     * @return the corresponding budget DTO.
     */
    public BudgetDTO getBudgetById(int id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        return convertToDTO(budget);
    }

    /**
     * Adds a new budget.
     *
     * @param budgetDTO the budget DTO containing the data to add.
     * @return the created budget DTO.
     */
    public BudgetDTO addBudget(BudgetDTO budgetDTO) {
        Budget budget = convertToEntity(budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    /**
     * Updates an existing budget.
     *
     * @param id        the ID of the budget to update.
     * @param budgetDTO the new budget data.
     * @return the updated budget DTO.
     */
    public BudgetDTO updateBudget(int id, BudgetDTO budgetDTO) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException(id));
        updateEntity(budget, budgetDTO);
        budget = budgetRepository.save(budget);
        return convertToDTO(budget);
    }

    /**
     * Deletes a budget by its ID.
     *
     * @param id the ID of the budget to delete.
     */
    public void deleteBudget(int id) {
        if (!budgetRepository.existsById(id)) {
            throw new BudgetNotFoundException(id);
        }
        budgetRepository.deleteById(id);
    }

    /**
     * Converts a Budget entity to a BudgetDTO.
     *
     * @param budget the budget entity.
     * @return the corresponding budget DTO.
     */
    private BudgetDTO convertToDTO(Budget budget) {
        return new BudgetDTO(
                budget.getId(),
                budget.getUser().getId(),
                budget.getCategory() != null ? budget.getCategory().getId() : null, // Handle nullable category
                budget.getAmount(),
                budget.getMonth(),
                budget.getYear()
        );
    }

    /**
     * Converts a BudgetDTO to a Budget entity.
     *
     * @param dto the budget DTO.
     * @return the corresponding budget entity.
     */
    private Budget convertToEntity(BudgetDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + dto.getUserId()));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
        }

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category); // Can be null if no category is provided
        budget.setAmount(dto.getAmount());
        budget.setMonth(dto.getMonth());
        budget.setYear(dto.getYear());
        return budget;
    }

    /**
     * Updates an existing Budget entity with data from a DTO.
     *
     * @param budget the existing budget entity.
     * @param dto    the new budget data.
     */
    private void updateEntity(Budget budget, BudgetDTO dto) {
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryId()));
            budget.setCategory(category);
        } else {
            budget.setCategory(null); // Allow removing the category
        }
        budget.setAmount(dto.getAmount());
        budget.setMonth(dto.getMonth());
        budget.setYear(dto.getYear());
    }



    /**
     * Retrieves all budgets for a specified user.
     *
     * @param userId the ID of the user whose budgets are to be retrieved
     * @return a list of BudgetDTO objects, or an empty list if no budgets are found
     */
    public List<BudgetDTO> findAllByUserId(int userId) {
        try {
            List<BudgetDTO> budgets = budgetRepository.findAllByUserId(userId);
            if (budgets.isEmpty()) {
                System.out.println("No budgets found for user ID: " + userId);
                return Collections.emptyList();  // Return an empty list if no budgets found
            }
            return budgets;
        } catch (DataAccessException e) {
            System.err.println("Error accessing data for user ID: " + userId + ", error: " + e.getMessage());
            throw new RuntimeException("Data access error when trying to retrieve budgets", e);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred for user ID: " + userId + ", error: " + e.getMessage());
            throw new RuntimeException("Unexpected error when trying to retrieve budgets", e);
        }
    }
}
