package com.expensetracker.service;

import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;
import com.expensetracker.exceptions.IncomeNotFoundException;
import com.expensetracker.exceptions.InvalidIncomeDataException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.IncomeRepository;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public List<IncomeDTO> getAllIncomes() {
        List<Income> incomes = incomeRepository.findAll();
        return incomes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public IncomeDTO getIncomeById(int id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income with ID " + id + " not found"));
        return convertToDTO(income);
    }

    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        try {
            Income income = convertToEntity(incomeDTO);
            income = incomeRepository.save(income);
            return convertToDTO(income);
        } catch (IllegalArgumentException e) {
            throw new InvalidIncomeDataException("Invalid data for income creation: " + e.getMessage());
        }
    }

    public IncomeDTO updateIncome(int id, IncomeDTO incomeDTO) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income with ID " + id + " not found"));

        try {
            updateEntity(income, incomeDTO);
            income = incomeRepository.save(income);
            return convertToDTO(income);
        } catch (IllegalArgumentException e) {
            throw new InvalidIncomeDataException("Invalid data for income update: " + e.getMessage());
        }
    }

    public void deleteIncome(int id) {
        if (!incomeRepository.existsById(id)) {
            throw new IncomeNotFoundException("Income with ID " + id + " not found");
        }
        incomeRepository.deleteById(id);
    }

    private IncomeDTO convertToDTO(Income income) {
        IncomeDTO dto = new IncomeDTO();
        dto.setId(income.getId());
        dto.setUserId(income.getUser().getId());
        dto.setCategoryId(income.getCategory().getId());
        dto.setAmount(income.getAmount());
        dto.setDate(income.getDate());
        dto.setDescription(income.getDescription());
        dto.setSource(income.getSource());
        return dto;
    }

    private Income convertToEntity(IncomeDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new InvalidIncomeDataException("User not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new InvalidIncomeDataException("Category not found"));

        Income income = new Income();
        income.setUser(user);
        income.setCategory(category);
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setDescription(dto.getDescription());
        income.setSource(dto.getSource());
        return income;
    }

    private void updateEntity(Income income, IncomeDTO dto) {
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setDescription(dto.getDescription());
        income.setSource(dto.getSource());
    }
}
