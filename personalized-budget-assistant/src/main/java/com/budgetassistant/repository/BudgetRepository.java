package com.budgetassistant.repository;

import com.budgetassistant.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    // Custom method to fetch all budgets for a specific user
    List<Budget> findByUserId(Long userId);
    
    // Custom method to find a budget for a specific user, category, month, and year
    Optional<Budget> findByUserIdAndCategoryAndMonthAndYear(Long userId, String category, Integer month, Integer year);
}