package com.foodie.app.service.impl;

import com.foodie.app.dto.CustomerRegistrationDto;
import com.foodie.app.entity.Admin;
import com.foodie.app.entity.Cart;
import com.foodie.app.entity.Customer;
import com.foodie.app.entity.Role;
import com.foodie.app.entity.User;
import com.foodie.app.exception.DuplicateResourceException;
import com.foodie.app.exception.ResourceNotFoundException;
import com.foodie.app.repository.AdminRepository;
import com.foodie.app.repository.CustomerRepository;
import com.foodie.app.repository.RoleRepository;
import com.foodie.app.repository.UserRepository;
import com.foodie.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Customer registerCustomer(CustomerRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An account with email " + dto.getEmail() + " already exists.");
        }

        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name("ROLE_CUSTOMER")
                        .description("Customer with browsing and ordering privileges")
                        .build()));

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isEnabled(true)
                .roles(new HashSet<>(Collections.singletonList(customerRole)))
                .build();

        User savedUser = userRepository.save(user);

        Customer customer = Customer.builder()
                .user(savedUser)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .defaultDeliveryAddress(dto.getDefaultDeliveryAddress())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .build();

        Cart initialCart = Cart.builder()
                .customer(customer)
                .totalAmount(BigDecimal.ZERO)
                .build();
        customer.setCart(initialCart);

        return customerRepository.save(customer);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<Customer> findCustomerByUserEmail(String email) {
        return customerRepository.findByUserEmail(email);
    }

    @Override
    public Optional<Customer> findCustomerById(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Optional<Admin> findAdminByUserEmail(String email) {
        return adminRepository.findByUserEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllWithUserAndOrders();
    }

    @Override
    @Transactional
    public Customer updateCustomer(Customer customer) {
        if (customer.getUser() != null) {
            userRepository.save(customer.getUser());
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        User user = customer.getUser();
        customerRepository.delete(customer);
        customerRepository.flush();
        if (user != null) {
            userRepository.delete(user);
        }
    }
}
