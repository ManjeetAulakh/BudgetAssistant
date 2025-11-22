package com.budgetassistant.service;

import com.budgetassistant.model.Budget;
import com.budgetassistant.model.Transaction;
import com.budgetassistant.model.TransactionType;
import com.budgetassistant.model.User;
import com.budgetassistant.payload.BudgetStatus;
import com.budgetassistant.repository.BudgetRepository;
import com.budgetassistant.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User) userService.loadUserByUsername(username);
    }

    // --- CREATE/UPDATE Budget ---
    public Budget save(Budget budget) {
        budget.setUser(getCurrentUser());
        return budgetRepository.save(budget);
    }

    // --- READ ALL Budgets for User ---
    public List<Budget> findAllByCurrentUser() {
        User currentUser = getCurrentUser();
        // Since User ID is now Long, this call is correct.
        return budgetRepository.findByUserId(currentUser.getId());
    }
    
    // --- READ ONE (with secure ownership check) ---
    public Optional<Budget> findById(Long id) {
        User currentUser = getCurrentUser();
        Optional<Budget> budgetOpt = budgetRepository.findById(id);

        // FINAL CLEANUP: Safe object comparison using equals()
        if (budgetOpt.isPresent() && 
            currentUser.getId().equals(budgetOpt.get().getUser().getId())) {
            return budgetOpt;
        }
        return Optional.empty();
    }
    
    // --- DELETE (with ownership check) ---
    public boolean delete(Long id) {
        Optional<Budget> budgetOpt = findById(id);
        if (budgetOpt.isPresent()) {
            budgetRepository.delete(budgetOpt.get());
            return true;
        }
        return false;
    }

    // --- CORE BUSINESS LOGIC: Calculate Current Spend ---
    public BigDecimal calculateCurrentSpend(Budget budget) {
        User currentUser = getCurrentUser();
        
        List<Transaction> transactions = transactionRepository.findByUserId(currentUser.getId());

        // Filter and sum expenses matching the budget's criteria
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> t.getCategory().equalsIgnoreCase(budget.getCategory()))
                // Use intValue() for comparison as Month/Year are Integers
                .filter(t -> t.getDate().atZone(java.time.ZoneId.systemDefault()).getYear() == budget.getYear().intValue()) 
                .filter(t -> t.getDate().atZone(java.time.ZoneId.systemDefault()).getMonthValue() == budget.getMonth().intValue())
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // A comprehensive status method used by the frontend:
    public BudgetStatus getBudgetStatus(Long budgetId) {
        Optional<Budget> budgetOpt = findById(budgetId);
        if (budgetOpt.isEmpty()) {
            return null;
        }
        
        Budget budget = budgetOpt.get();
        BigDecimal spent = calculateCurrentSpend(budget);
        BigDecimal remaining = budget.getLimitAmount().subtract(spent);

        return new BudgetStatus(budget.getCategory(), budget.getLimitAmount(), spent, remaining);
    }
}