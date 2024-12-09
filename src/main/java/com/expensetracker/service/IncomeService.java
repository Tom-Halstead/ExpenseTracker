package com.expensetracker.service;

import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.Income;
import com.expensetracker.entity.User;
import com.expensetracker.exception.IncomeNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.IncomeRepository;
import com.expensetracker.repository.UserRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public List<IncomeDTO> getAllIncomes() {
        List<Income> incomes = incomeRepository.findAll();
        return incomes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public IncomeDTO getIncomeById(int id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income with ID " + id + " not found"));
        return convertToDTO(income);
    }
    @Transactional
    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        Income income = convertToEntity(incomeDTO);
        income = incomeRepository.save(income);
        return convertToDTO(income);
    }
    @Transactional
    public IncomeDTO updateIncome(int id, IncomeDTO incomeDTO) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IncomeNotFoundException("Income with ID " + id + " not found"));
        updateEntityWithDTO(income, incomeDTO);
        income = incomeRepository.save(income);
        return convertToDTO(income);
    }
    @Transactional
    public void deleteIncome(int id) {
        if (!incomeRepository.existsById(id)) {
            throw new IncomeNotFoundException("Income with ID " + id + " not found");
        }
        incomeRepository.deleteById(id);
    }
    @Transactional
    private IncomeDTO convertToDTO(Income income) {
        IncomeDTO dto = new IncomeDTO();
        dto.setId(income.getId());
        dto.setUserId(income.getUser().getId());
        dto.setCategoryId(income.getCategory().getId());
        dto.setAmount(income.getAmount());
        dto.setDate(income.getDate());
        dto.setDescription(income.getDescription());
        dto.setSource(income.getSource());
        dto.setCreatedAt(income.getCreatedAt());  // Directly using the timestamp set by JPA
        dto.setUpdatedAt(income.getUpdatedAt());  // Directly using the timestamp set by JPA
        return dto;
    }
    @Transactional
    private Income convertToEntity(IncomeDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IncomeNotFoundException("User not found"));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IncomeNotFoundException("Category not found"));

        Income income = new Income();
        income.setId(dto.getId()); // Set ID in case of update
        income.setUser(user);
        income.setCategory(category);
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setDescription(dto.getDescription());
        income.setSource(dto.getSource());
        return income;
    }
    @Transactional
    private void updateEntityWithDTO(Income income, IncomeDTO dto) {
        income.setAmount(dto.getAmount());
        income.setDate(dto.getDate());
        income.setDescription(dto.getDescription());
        income.setSource(dto.getSource());
    }
}
