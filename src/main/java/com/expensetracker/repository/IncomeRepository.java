package com.expensetracker.repository;

import com.expensetracker.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncomeRepository extends JpaRepository<Income, Integer> {

    Optional<Income> findById(int id);


    void deleteById(int id);
}
