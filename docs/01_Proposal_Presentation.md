# University Software Engineering Project: Proposal Presentation
## Project: Web-based Food Ordering System
**Course:** SE2030 - Software Engineering  
**Technology Stack:** Java 21 • Spring Boot 3 • Spring Security • Spring Data JPA • MySQL • Thymeleaf • Bootstrap 5

---

### Slide 1: Title & Team Overview
- **Project Title:** Web-based Food Ordering System
- **Context:** Automated Online Food Ordering, Inventory Synchronization & Executive Financial Analytics
- **Target Users:** Customers & Restaurant Administrators
- **Tech Stack Overview:** Java 21, Spring Boot, MySQL, Bootstrap 5, Thymeleaf

---

### Slide 2: Problem Statement & Motivation
- **Manual Ordering Friction:** Phone queues, communication errors, and slow peak-hour order turnaround.
- **Stock Mismatches:** Out-of-sync inventory leading to orders placed for sold-out dishes.
- **Financial Blindspots:** Lack of automated profit/expense tracking; difficulty calculating real-time COGS and net margins.
- **Security Deficits:** Unprotected personal data and lack of structured payment audit trails.

---

### Slide 3: Proposed Solution & Key Objectives
- **Modern Customer Experience:** Seamless registration, menu search/filtering, cart calculation, simulated checkout, and order status tracking.
- **Automated Inventory Synchronization:** Atomic stock deduction upon order completion and instant restocking upon valid cancellation.
- **Executive Administration:** Centralized menu CRUD, kitchen dispatch queue, expense logging, and real-time Sales & Net Profit reports.
- **Enterprise Engineering Standards:** Clean MVC, Spring Security RBAC, BCrypt cryptography, and ACID-compliant transactional persistence.

---

### Slide 4: System Architecture & Technologies
- **Presentation Layer:** Bootstrap 5.3 + Thymeleaf + JavaScript ES6
- **Security Layer:** Spring Security 6.x (BCrypt, CSRF Defense, RBAC: `ROLE_CUSTOMER`, `ROLE_ADMIN`)
- **Controller & Service Layer:** Spring Boot 3.x MVC & REST Controllers with `@Transactional` service logic
- **Persistence Layer:** Spring Data JPA + Hibernate ORM
- **Database:** MySQL 8.0 (InnoDB) with 16 normalized relational tables

---

### Slide 5: Core Modules Breakdown
- **Customer Modules:**
  1. Registration & Login (BCrypt, Validation)
  2. Menu Discovery (Categories, Search, Price/Promo Filter)
  3. Dynamic Cart & Auto-Tax/Delivery Calculation
  4. Simulated Card Checkout & Payment Handling
  5. Live Status Tracking & Policy-Enforced Cancellation (`PENDING`/`CONFIRMED` only)
- **Admin Modules:**
  1. Executive KPI Dashboard (Today's Revenue, Active Orders, Low Stock Alerts)
  2. Category & Food Item CRUD
  3. Stock Management & Low-Stock Alerts (< 10 units)
  4. Order Queue & Kitchen State Progression
  5. Expense Tracking & Daily/Monthly Sales & Net Profit Reporting

---

### Slide 6: Database & Data Integrity
- **16 Core Entities:** `Users`, `Roles`, `Customers`, `Admins`, `FoodCategory`, `Food`, `Inventory`, `Cart`, `CartItem`, `Orders`, `OrderItem`, `Payments`, `Expenses`, `Sales`, `Reports`, `UserRoles`
- **Integrity Highlights:**
  - Foreign key cascades & index optimization
  - High-precision monetary storage via `DECIMAL(10,2)` / `BigDecimal`
  - Immutable payment transaction records for auditability

---

### Slide 7: Project Plan & Agile Roadmap
- **Sprint 1 (Weeks 1-2):** Requirements, Database Schema, UML Modeling, Spring Boot Project Setup
- **Sprint 2 (Weeks 3-4):** Authentication, Role-Based Security, Catalog & Food Management
- **Sprint 3 (Weeks 5-6):** Cart, Checkout, Simulated Payment Gateway, and Atomic Order Lifecycle
- **Sprint 4 (Weeks 7-8):** Inventory Auto-Deduction, Expense Tracking, Financial Reporting & Polish
- **Sprint 5 (Weeks 9-10):** Unit/Integration Testing, Documentation, User Manual & Final Presentation

---
