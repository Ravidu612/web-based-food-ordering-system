package com.foodie.app.service.impl;

import com.foodie.app.dto.ExpenseDto;
import com.foodie.app.entity.Admin;
import com.foodie.app.entity.Expense;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.AdminRepository;
import com.foodie.app.repository.ExpenseRepository;
import com.foodie.app.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final AdminRepository adminRepository;

    @Override
    @Transactional
    public Expense createExpense(ExpenseDto dto, String adminEmail) {
        Admin admin = adminRepository.findByUserEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with email: " + adminEmail));

        Expense expense = Expense.builder()
                .recordedByAdmin(admin)
                .expenseCategory(dto.getExpenseCategory())
                .title(dto.getTitle())
                .amount(dto.getAmount())
                .expenseDate(dto.getExpenseDate())
                .description(dto.getDescription())
                .build();

        return expenseRepository.save(expense);
    }

    @Override
    public List<ExpenseDto> getExpensesBetween(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findByExpenseDateBetweenOrderByExpenseDateDesc(startDate, endDate).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDto> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .sorted((a, b) -> b.getExpenseDate().compareTo(a.getExpenseDate()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Expense updateExpense(Long id, ExpenseDto dto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        
        expense.setExpenseCategory(dto.getExpenseCategory());
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setDescription(dto.getDescription());
        
        return expenseRepository.save(expense);
    }

    private ExpenseDto mapToDto(Expense expense) {
        return ExpenseDto.builder()
                .id(expense.getId())
                .expenseCategory(expense.getExpenseCategory())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .description(expense.getDescription())
                .recordedByName(expense.getRecordedByAdmin().getFullName())
                .build();
    }
}
