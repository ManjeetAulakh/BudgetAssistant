// src/data/mockData.js

export const mockUser = {
  username: "AlexDev",
  email: "alex@example.com"
};

export const mockTransactions = [
  { id: 1, description: "Freelance Project", amount: 1200.00, category: "Income", date: "2025-11-20", type: "INCOME" },
  { id: 2, description: "Grocery Run", amount: 150.50, category: "Groceries", date: "2025-11-21", type: "EXPENSE" },
  { id: 3, description: "Netflix Subscription", amount: 15.00, category: "Entertainment", date: "2025-11-22", type: "EXPENSE" },
  { id: 4, description: "Gas Station", amount: 45.00, category: "Transport", date: "2025-11-23", type: "EXPENSE" },
  { id: 5, description: "Monthly Rent", amount: 800.00, category: "Housing", date: "2025-11-01", type: "EXPENSE" },
  { id: 6, description: "Coffee Shop", amount: 5.50, category: "Dining", date: "2025-11-24", type: "EXPENSE" },
];

export const mockBudgets = [
  { id: 1, category: "Groceries", limitAmount: 500.00, spent: 350.00, remaining: 150.00, color: "#8884d8" },
  { id: 2, category: "Entertainment", limitAmount: 200.00, spent: 180.00, remaining: 20.00, color: "#82ca9d" },
  { id: 3, category: "Transport", limitAmount: 300.00, spent: 120.00, remaining: 180.00, color: "#ffc658" },
  { id: 4, category: "Dining", limitAmount: 150.00, spent: 45.00, remaining: 105.00, color: "#ff8042" },
];

export const mockPrediction = {
  nextMonthTotal: 1450.00,
  insight: "Spending is projected to increase by 5% due to holiday season."
};