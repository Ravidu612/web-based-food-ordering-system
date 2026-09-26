package com.foodie.app.repository;

import com.foodie.app.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByOrderId(Long orderId);

    List<Sale> findBySaleDateBetweenOrderBySaleDateDesc(LocalDate startDate, LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.totalRevenue), 0.00) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumRevenueBetweenDates(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.totalCogs), 0.00) FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumCogsBetweenDates(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);
}
