package com.budgetassistant.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "budgets")
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the User who owns this budget
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String category; // Must match the category in Transaction

    @Column(nullable = false)
    private BigDecimal limitAmount; // The maximum allowed spending

    @Column(nullable = false)
    private Integer month; // 1 to 12
    
    @Column(nullable = false)
    private Integer year; // e.g., 2025

    // Convenience field: Calculated by the service layer, not stored here, but useful.
    // We will calculate current spend in the service layer, not store it as a column.
}