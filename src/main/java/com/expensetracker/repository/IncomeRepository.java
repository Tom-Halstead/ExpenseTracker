package com.expensetracker.repository;

import com.expensetracker.model.Category;
import com.expensetracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    Optional<Income> findById(int id);


    void deleteById(int id);
}
