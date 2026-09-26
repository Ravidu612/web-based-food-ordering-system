package com.foodie.app.repository;

import com.foodie.app.entity.Customer;
import com.foodie.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findByUserEmail(String email);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.orders")
    List<Customer> findAllWithUserAndOrders();
}

