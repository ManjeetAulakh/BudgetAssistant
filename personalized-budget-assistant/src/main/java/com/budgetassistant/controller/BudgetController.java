package com.budgetassistant.controller;

import com.budgetassistant.model.Budget;
import com.budgetassistant.payload.BudgetStatus;
import com.budgetassistant.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    // POST /api/budgets
    // CREATE: Creates a new budget limit for the current user.
    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget savedBudget = budgetService.save(budget);
        return new ResponseEntity<>(savedBudget, HttpStatus.CREATED);
    }

    // GET /api/budgets
    // READ: Retrieves all budgets belonging to the current authenticated user.
    @GetMapping
    public ResponseEntity<List<Budget>> getAllUserBudgets() {
        List<Budget> budgets = budgetService.findAllByCurrentUser();
        return ResponseEntity.ok(budgets);
    }
    
    // GET /api/budgets/{id}/status
    // READ STATUS: Retrieves the calculated spending status for a specific budget.
    @GetMapping("/{id}/status")
    public ResponseEntity<BudgetStatus> getBudgetStatus(@PathVariable Long id) {
        BudgetStatus status = budgetService.getBudgetStatus(id);
        if (status != null) {
            return ResponseEntity.ok(status);
        }
        return ResponseEntity.notFound().build();
    }

    // PUT /api/budgets/{id}
    // UPDATE: Updates a budget limit (e.g., changing the limit amount).
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budgetDetails) {
        return budgetService.findById(id)
                .map(existingBudget -> {
                    // Update only mutable fields
                    existingBudget.setCategory(budgetDetails.getCategory()); 
                    existingBudget.setLimitAmount(budgetDetails.getLimitAmount());
                    existingBudget.setMonth(budgetDetails.getMonth());
                    existingBudget.setYear(budgetDetails.getYear());
                    
                    Budget updatedBudget = budgetService.save(existingBudget);
                    return ResponseEntity.ok(updatedBudget);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/budgets/{id}
    // DELETE: Deletes a budget.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        if (budgetService.delete(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build();
    }
}