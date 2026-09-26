package com.foodie.app.service;

import com.foodie.app.dto.CustomerRegistrationDto;
import com.foodie.app.entity.Admin;
import com.foodie.app.entity.Customer;
import com.foodie.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Customer registerCustomer(CustomerRegistrationDto registrationDto);
    Optional<User> findUserByEmail(String email);
    Optional<Customer> findCustomerByUserEmail(String email);
    Optional<Customer> findCustomerById(Long customerId);
    Optional<Admin> findAdminByUserEmail(String email);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Long customerId);
}
