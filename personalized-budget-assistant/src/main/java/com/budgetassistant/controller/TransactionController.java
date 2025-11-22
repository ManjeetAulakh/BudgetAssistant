package com.budgetassistant.controller;

import com.budgetassistant.model.Transaction;
import com.budgetassistant.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // POST /api/transactions
    // CREATE: Adds a new transaction for the current authenticated user.
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.save(transaction);
        return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
    }

    // GET /api/transactions
    // READ: Retrieves all transactions belonging to the current authenticated user.
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllUserTransactions() {
        List<Transaction> transactions = transactionService.findAllByCurrentUser();
        return ResponseEntity.ok(transactions);
    }

    // GET /api/transactions/{id}
    // READ: Retrieves a specific transaction, performing an ownership check.
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/transactions/{id}
    // UPDATE: Updates an existing transaction, performing an ownership check.
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails) {
        return transactionService.findById(id)
                .map(existingTransaction -> {
                    // Update fields from the request body
                    existingTransaction.setAmount(transactionDetails.getAmount());
                    existingTransaction.setDescription(transactionDetails.getDescription());
                    existingTransaction.setCategory(transactionDetails.getCategory());
                    existingTransaction.setDate(transactionDetails.getDate());
                    existingTransaction.setType(transactionDetails.getType());
                    
                    // Save the updated entity (Service handles ownership)
                    Transaction updatedTransaction = transactionService.save(existingTransaction);
                    return ResponseEntity.ok(updatedTransaction);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/transactions/{id}
    // DELETE: Deletes a transaction, performing an ownership check.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        if (transactionService.delete(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}