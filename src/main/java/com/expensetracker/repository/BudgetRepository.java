package com.expensetracker.repository;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    Optional<Budget> findById(int id);

    void deleteById(int id);

    List<BudgetDTO> findAllByUserId(int userId);
}
