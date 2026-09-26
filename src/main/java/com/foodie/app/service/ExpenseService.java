package com.foodie.app.service;

import com.foodie.app.dto.ExpenseDto;
import com.foodie.app.entity.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    Expense createExpense(ExpenseDto expenseDto, String adminEmail);
    List<ExpenseDto> getExpensesBetween(LocalDate startDate, LocalDate endDate);
    List<ExpenseDto> getAllExpenses();
    void deleteExpense(Long id);
    Expense updateExpense(Long id, ExpenseDto expenseDto);
}
