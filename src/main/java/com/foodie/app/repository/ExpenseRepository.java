package com.foodie.app.repository;

import com.foodie.app.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByExpenseDateBetweenOrderByExpenseDateDesc(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0.00) FROM Expense e WHERE e.expenseDate BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesBetweenDates(@Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
