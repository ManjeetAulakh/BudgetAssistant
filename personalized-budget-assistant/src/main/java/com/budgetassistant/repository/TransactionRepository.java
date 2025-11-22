package com.budgetassistant.repository;

import com.budgetassistant.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Custom method to fetch all transactions for a specific user
    List<Transaction> findByUserId(Long userId);
}