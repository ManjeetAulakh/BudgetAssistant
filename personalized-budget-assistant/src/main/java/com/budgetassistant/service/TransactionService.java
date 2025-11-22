package com.budgetassistant.service;

import com.budgetassistant.model.Transaction;
import com.budgetassistant.model.User;
import com.budgetassistant.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService; 

    // Helper method to retrieve the current User
    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User) userService.loadUserByUsername(username);
    }

    // --- CREATE ---
    public Transaction save(Transaction transaction) {
        transaction.setUser(getCurrentUser());
        return transactionRepository.save(transaction);
    }

    // --- READ ALL for User (Fix for findByUserId argument) ---
    public List<Transaction> findAllByCurrentUser() {
        User currentUser = getCurrentUser();
        // FIX 1: Explicitly cast to Long to satisfy the repository method signature
        return transactionRepository.findByUserId(currentUser.getId());
    }

    // --- READ ONE (with secure ownership check) ---
    public Optional<Transaction> findById(Long id) {
        User currentUser = getCurrentUser();
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);

        // FIX 2: Use longValue() on both sides and primitive equality (==) 
        // This avoids the "Cannot invoke equals(int)" error entirely.
        if (transactionOpt.isPresent() && 
            transactionOpt.get().getUser().getId().longValue() == currentUser.getId().longValue()) {
            return transactionOpt;
        }
        return Optional.empty();
    }
    
    // --- DELETE (with ownership check) ---
    public boolean delete(Long id) {
        Optional<Transaction> transactionOpt = findById(id);
        
        if (transactionOpt.isPresent()) {
            transactionRepository.delete(transactionOpt.get());
            return true;
        }
        return false;
    }
}