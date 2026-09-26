package com.foodie.app.repository;

import com.foodie.app.entity.Order;
import com.foodie.app.entity.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId);

    Page<Order> findByCustomerIdOrderByOrderDateDesc(Long customerId, Pageable pageable);

    List<Order> findByOrderStatusOrderByOrderDateDesc(OrderStatus orderStatus);

    Page<Order> findAllByOrderByOrderDateDesc(Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderStatus IN ('CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY')")
    long countActiveOrders();

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);
}
