package com.foodie.app.repository;

import com.foodie.app.entity.Admin;
import com.foodie.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
    Optional<Admin> findByUserEmail(String email);
}
