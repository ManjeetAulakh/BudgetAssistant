package com.budgetassistant.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BudgetStatus {
    private String category;
    private BigDecimal limit;
    private BigDecimal spent;
    private BigDecimal remaining;
}