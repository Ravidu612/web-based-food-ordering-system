# Software Engineering Project Proposal & Requirements Specification (SRS)
## Project Title: Web-based Food Ordering System
**Course:** SE2030 - Software Engineering  
**Architecture Style:** Multi-Tier MVC & RESTful Architecture  
**Target Platform:** Java-based Web Application (Spring Boot 3 + MySQL + Bootstrap 5 + Thymeleaf)

---

## 1. Executive Summary & Project Proposal

The **Web-based Food Ordering System** is an enterprise-grade, multi-tier web application engineered to modernize and digitize food service operations for restaurants. The platform establishes a seamless digital bridge between **Customers**—who demand convenient menu discovery, responsive cart management, secure checkout, simulated online payments, and live order tracking—and **Restaurant Administrators**—who require granular back-office control over categorized menus, real-time inventory levels, dynamic order dispatching, operational expenses, and executive financial analytics (Daily/Monthly Sales, Cost of Goods Sold, and Net Profit).

---

## 2. Problem Statement

Traditional dine-in and phone-based restaurant ordering models suffer from critical operational bottlenecks:
1. **Customer Inconvenience & Order Errors:** Manual telephone orders and physical queues result in order miscommunications, long wait times during peak hours, and lack of real-time item availability feedback.
2. **Inventory Stockout & Wastage:** Disconnected stock tracking causes restaurants to accept orders for unavailable ingredients or mismanage perishable stock.
3. **Financial Opacity & Fragmented Reporting:** Restaurant management struggles to compute real-time gross margins, correlate daily operating expenses (labor, utilities, procurement) with revenue, and identify top-performing food categories without manual reconciliation.
4. **Security & Data Vulnerabilities:** Unsecured paper receipts and non-standardized digital tools expose customer data and lack verifiable audit trails for payments.

---

## 3. Project Objectives

1. **Develop an Intuitive Customer Portal:** Provide an interactive, responsive web interface for self-registration, BCrypt-authenticated login, catalog search/filter, dynamic cart operations, and streamlined checkout.
2. **Implement Secure Simulated Payments & Order Lifecycle:** Enable atomic order placement integrated with mock payment gateway verification and real-time state tracking (`PENDING`, `CONFIRMED`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`).
3. **Automate Inventory Synchronization:** Atomically decrement ingredient/food stock upon order placement and automatically replenish stock if an order is cancelled prior to preparation.
4. **Deliver an Executive Back-Office & Financial Analytics Suite:** Provide administrators with real-time KPI dashboards, category/food CRUD management, order dispatch queues, customer registry oversight, expense logging, and automated Sales & Net Profit reports.
5. **Adhere to Rigorous Software Engineering Principles:** Build the solution using **Java 21**, **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **MySQL**, and **Clean MVC Architecture**, adhering strictly to **SOLID** and **OOP** design patterns.

---

## 4. Project Scope

### 4.1 In-Scope Capabilities
- **Customer Subsystem:**
  - Registration with email validation and BCrypt password encryption.
  - Role-based login (`ROLE_CUSTOMER`), profile management, and session handling.
  - Menu catalog browsing with category tabs, keyword search, price/promotion filters, and item details.
  - Dynamic shopping cart with live subtotal, tax, and delivery fee calculation.
  - Checkout workflow with delivery address capture and order review.
  - Simulated online payment gateway (Card validation, Success/Failure processing, audit logging).
  - Order tracking, historical order ledger, and conditional order cancellation.
- **Administrator Subsystem:**
  - Secure Admin authentication (`ROLE_ADMIN`) and dedicated executive dashboard.
  - Food Category & Food Item CRUD operations (with image URLs, base pricing, preparation times).
  - Inventory & Stock level management with low-stock alerts (< 10 units).
  - Order lifecycle management and kitchen status progression.
  - Customer account management and audit views.
  - Payment transaction audit ledger.
  - Operational Expense logging (Procurement, Utilities, Salaries, Maintenance).
  - Financial Analytics & Reporting: Daily Sales, Monthly Sales, Expense Reports, and Net Profit calculations.

### 4.2 Out-of-Scope (Project Boundaries & Limitations)
- Multi-tenant restaurant marketplace (the system is designed for a dedicated restaurant brand/franchise).
- Live GPS satellite telematics tracking for drivers (order tracking follows deterministic status milestones).
- Physical hardware POS printer/card terminal integration.
- Multi-currency conversion (system standardizes on a single base currency).

---

## 5. Assumptions and Limitations

### 5.1 Assumptions
- End users have access to standard modern web browsers (Chrome, Edge, Firefox, Safari) with JavaScript enabled.
- Restaurant administrators update stock levels during daily opening/replenishment cycles and record daily operational expenses.
- All financial monetary calculations use `java.math.BigDecimal` and database `DECIMAL(10,2)` to avoid floating-point inaccuracies.

### 5.2 Limitations
- The online payment module uses a simulated gateway interface designed for academic demonstration and testing without real merchant bank settlements.
- Order cancellation by customers is strictly time-bounded: permissible only prior to the kitchen setting the status to `PREPARING`.

---

## 6. Stakeholder Analysis

| Stakeholder | Role & Involvement | Value Proposition / Expectations |
| :--- | :--- | :--- |
| **Customer** | External Primary User | Fast, frictionless ordering, transparent pricing, live order updates, and secure account management. |
| **Restaurant Administrator** | Internal Primary User | Unified control over menus, real-time inventory alerts, order dispatching, expense tracking, and financial analytics. |
| **Kitchen / Dispatch Staff** | Internal Operational User | Clear itemized order queues, delivery instructions, and timely status transition controls. |
| **Academic Supervisor / Assessor** | Educational Evaluator | High-quality software architecture, adherence to SOLID/OOP/MVC, clean code, thorough documentation, and full test coverage. |

---

## 7. Functional Requirements (FR)

### 7.1 Customer Module Requirements

| ID | Requirement Name | Description | Priority |
| :--- | :--- | :--- | :--- |
| **FR-CUST-01** | User Registration | System shall allow new customers to register with full name, unique email, phone number, address, and password. | **Must Have** |
| **FR-CUST-02** | Registration Validation | System shall validate email formatting, minimum password length (8+ chars), and required fields. | **Must Have** |
| **FR-CUST-03** | Customer Login | System shall authenticate customers via email and password using BCrypt hashing. | **Must Have** |
| **FR-CUST-04** | Role Authorization | System shall restrict customer access to `/customer/**`, `/cart/**`, `/orders/**` routes. | **Must Have** |
| **FR-CUST-05** | Category Browsing | System shall list all active food categories with badge counts of available dishes. | **Must Have** |
| **FR-CUST-06** | Food Search & Filter | System shall provide instant search by title/keywords and filtering by category, price, and active discounts. | **Must Have** |
| **FR-CUST-07** | Food Detail View | System shall render complete food details including description, price, prep time, availability, and promotions. | **Must Have** |
| **FR-CUST-08** | Shopping Cart Actions | System shall allow adding items, updating quantities, and removing items from the cart. | **Must Have** |
| **FR-CUST-09** | Cart Total Calculation | System shall compute real-time line item subtotals, tax (e.g. 5%), delivery charge, and grand total. | **Must Have** |
| **FR-CUST-10** | Checkout Information | System shall capture delivery address, recipient contact phone, and delivery notes. | **Must Have** |
| **FR-CUST-11** | Payment Processing | System shall validate simulated card payment details (Card Number, Expiry, CVV) and generate a transaction token. | **Must Have** |
| **FR-CUST-12** | Payment State Handling | System shall record payment status (`SUCCESS` / `FAILED`) and only generate confirmed orders upon payment success. | **Must Have** |
| **FR-CUST-13** | Live Order Tracking | System shall display the current milestone status of active orders with visual progress indicators. | **Must Have** |
| **FR-CUST-14** | Order History Ledger | System shall display past orders with itemized lists, timestamp, total paid, and receipt references. | **Should Have** |
| **FR-CUST-15** | Order Cancellation | System shall allow the customer to cancel an order **only if** the status is `PENDING` or `CONFIRMED`. | **Must Have** |

### 7.2 Administrator Module Requirements

| ID | Requirement Name | Description | Priority |
| :--- | :--- | :--- | :--- |
| **FR-ADM-01** | Admin Authentication | System shall provide secure authentication for admin users with `ROLE_ADMIN`. | **Must Have** |
| **FR-ADM-02** | Executive Dashboard | System shall display KPI metrics: Today's Revenue, Active Orders, Low Stock Alerts, Total Customers. | **Must Have** |
| **FR-ADM-03** | Category Management | System shall allow admins to Create, Read, Update, and Deactivate Food Categories. | **Must Have** |
| **FR-ADM-04** | Food Management | System shall allow admins to Create, Edit, Activate/Deactivate Food items, set prices, and upload/assign images. | **Must Have** |
| **FR-ADM-05** | Inventory & Stock Control| System shall track unit quantities for each food item, enable restocking, and highlight low stock (< 10 units). | **Must Have** |
| **FR-ADM-06** | Order Queue & Dispatch | System shall allow admins to transition orders through: `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED` or `CANCELLED`. | **Must Have** |
| **FR-ADM-07** | Customer Oversight | System shall list all registered customers with profile details, contact info, and total order volume. | **Should Have** |
| **FR-ADM-08** | Payment Audit Ledger | System shall provide a complete audit trail of all transactions with reference codes, timestamps, and amounts. | **Must Have** |
| **FR-ADM-09** | Expense Logging | System shall allow recording operational expenses (Category, Amount, Date, Description). | **Must Have** |
| **FR-ADM-10** | Sales Reports | System shall generate Daily and Monthly sales reports with gross revenue and item popularity breakdown. | **Must Have** |
| **FR-ADM-11** | Profit & Loss Reports | System shall compute Net Profit = Gross Sales - (Cost of Goods Sold + Operating Expenses). | **Must Have** |
| **FR-ADM-12** | Business Analytics Export| System shall compile analytical summary reports for administrative review. | **Should Have** |

---

## 8. Non-Functional Requirements (NFR)

- **NFR-SEC-01 (Password Encryption):** Passwords hashed using **BCrypt** (work factor $\ge 10$). Plaintext passwords never stored or logged.
- **NFR-SEC-02 (Role-Based Access Control):** Administrative routes (`/admin/**`) strictly guarded by Spring Security filter chains.
- **NFR-SEC-03 (SQL Injection Prevention):** Zero raw string concatenation in SQL; all queries executed via JPA Parameterized JPQL/Criteria.
- **NFR-SEC-04 (XSS & CSRF Defense):** Automated HTML escaping via Thymeleaf and CSRF tokens enforced on all HTTP POST/PUT/DELETE forms.
- **NFR-PERF-01 (Response Time):** Catalog search and page rendering under **500 ms** for up to 100 concurrent sessions.
- **NFR-PERF-02 (Checkout Latency):** Complete atomic checkout and simulated payment execution completed within **1.5 seconds**.
- **NFR-REL-01 (Transactional Integrity):** Multi-table checkout updates (Order + OrderItems + Payment + Stock decrement) wrapped in `@Transactional` ensuring ACID compliance.
- **NFR-USE-01 (Usability & Responsiveness):** Responsive design across mobile (>= 375px), tablet, and desktop viewports via Bootstrap 5.
- **NFR-MAINT-01 (Clean Architecture):** Strict layered architecture: Controller $\rightarrow$ Service $\rightarrow$ Repository $\rightarrow$ Database Entity.
- **NFR-PORT-01 (Cross-Platform Portability):** Runs on any platform with JDK 21 LTS and MySQL 8.x.

---

## 9. Business Rules (BR)

- **BR-01 (Unique Identity):** Email address must be globally unique across all customer and admin accounts.
- **BR-02 (Stock Availability):** Customers cannot place an order for item quantities exceeding current inventory stock.
- **BR-03 (Automatic Inventory Decrement):** Upon successful checkout and payment, inventory stock is decremented in the same transaction.
- **BR-04 (Cancellation Guardrail):** Customer cancellation is permissible **only** while order status is `PENDING` or `CONFIRMED`. Once changed to `PREPARING` or later, cancellation is rejected.
- **BR-05 (Automatic Stock Restoration):** When an order is cancelled, all items and quantities are immediately returned to inventory stock.
- **BR-06 (Financial Immutability):** Records in `Payments`, `Sales`, and `Expenses` cannot be deleted; adjustments require balancing entries.
- **BR-07 (Net Profit Formula):**
  $$\text{Net Profit} = \text{Total Sales Revenue} - (\text{Cost of Goods Sold} + \text{Total Operating Expenses})$$

---

## 10. System Overview & Major/Minor Functions

### 10.1 High-Level Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                 Client Layer (Browser)                      │
│        HTML5 • CSS3 • Bootstrap 5 • JavaScript • Thymeleaf  │
└──────────────────────────────┬──────────────────────────────┘
                               │ HTTP / HTTPS (REST & MVC)
┌──────────────────────────────▼──────────────────────────────┐
│                  Security & Routing Layer                   │
│          Spring Security 6 (RBAC Filter Chain + CSRF)       │
└──────────────────────────────┬──────────────────────────────┘
                               │
┌──────────────────────────────▼──────────────────────────────┐
│                    Presentation Layer                       │
│     Web Controllers (@Controller) & REST APIs (@RestController)│
└──────────────────────────────┬──────────────────────────────┘
                               │ DTOs / View Models
┌──────────────────────────────▼──────────────────────────────┐
│                   Business Service Layer                    │
│      Services (@Service) • @Transactional Business Logic     │
└──────────────────────────────┬──────────────────────────────┘
                               │ Domain Entities
┌──────────────────────────────▼──────────────────────────────┐
│                   Data Persistence Layer                    │
│       Spring Data JPA Repositories • Hibernate ORM          │
└──────────────────────────────┬──────────────────────────────┘
                               │ JDBC
┌──────────────────────────────▼──────────────────────────────┐
│                   Relational Database                       │
│                     MySQL 8.0 (InnoDB)                      │
└─────────────────────────────────────────────────────────────┘
```

### 10.2 Major & Minor Functional Decomposition

1. **Authentication & Authorization Subsystem**
   - *Minor:* Customer Registration, Email Validation, BCrypt Authentication, Role-based Routing, Session Invalidation.
2. **Food Catalog & Search Engine**
   - *Minor:* Category Listing, Keyword Search, Multi-Filter (Price, Veg/Non-veg, Promo), Item Detail Card, Promotional Badging.
3. **Cart & Pricing Engine**
   - *Minor:* Session/DB Cart Persistence, Item Addition/Modification/Deletion, Real-time Subtotal, Tax and Delivery Fee Computation.
4. **Checkout & Order Management**
   - *Minor:* Delivery Address Capture, Order Review, Atomic Order Creation, Snapshot Line-Item Pricing, Order Status State Machine.
5. **Payment Gateway Simulation**
   - *Minor:* Card Input Format Checking, Token Generation, Success/Failure Status Logging, Payment Ledger Audit Record.
6. **Inventory & Stock Management**
   - *Minor:* Real-time Stock Tracking, Stock Decrement on Order, Stock Restock on Cancel, Admin Stock Adjustment, Low-Stock Notification.
7. **Admin Back-Office Subsystem**
   - *Minor:* Category CRUD, Food Item CRUD, Order Queue Dispatcher, Customer Account Directory.
8. **Financial Reporting & Analytics Engine**
   - *Minor:* Daily Sales Aggregator, Monthly Sales Aggregator, Expense Logger, Net Profit Calculator, Dashboard Analytics Charts.

---

## 11. Risk Analysis & Mitigation Matrix

| Risk ID | Risk Description | Likelihood | Impact | Severity | Mitigation Strategy |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **R-01** | **Inventory Race Condition:** Simultaneous orders for the last stock unit. | Medium | High | High | Enforce atomic stock checks and database-level row locking / pessimistic locking during order checkout. |
| **R-02** | **Payment/Order Inconsistency:** Payment completes but network drops before order is saved. | Low | Critical | Critical | Wrap order creation, payment record logging, and stock deduction inside a single Spring `@Transactional` block. |
| **R-03** | **Unauthorized Access:** Customer attempting to invoke `/admin/**` endpoints. | Medium | High | High | Enforce strict role-based route protection via Spring Security `SecurityFilterChain` configuring `.hasRole('ADMIN')`. |
| **R-04** | **Input Tampering & SQLi:** Malicious payload injection in query params or forms. | Low | High | High | Utilize Hibernate parameterized JPQL queries, Hibernate Validator annotations (`@Valid`, `@NotBlank`), and Thymeleaf escaping. |
| **R-05** | **Financial Rounding Errors:** Inaccurate decimal rounding across tax, discounts, and totals. | High | Medium | Medium | Mandate `BigDecimal` arithmetic with `RoundingMode.HALF_UP` across all entity models, DTOs, and database `DECIMAL(10,2)` columns. |

---

## 12. Technology Stack Justification

| Layer | Selected Tech | Justification |
| :--- | :--- | :--- |
| **Language** | **Java 21 (LTS)** | Strongly-typed, modern language with pattern matching, record classes, virtual threads, and long-term enterprise support. |
| **Framework** | **Spring Boot 3.x** | Enterprise gold standard; provides built-in dependency injection, embedded Tomcat server, declarative transactions, and automated configuration. |
| **Security** | **Spring Security 6.x** | Comprehensive authentication and role-based authorization framework with built-in protection against CSRF, session hijacking, and brute-force attacks. |
| **ORM / Data Access** | **Spring Data JPA / Hibernate** | Type-safe repository abstraction, automated CRUD generation, seamless relationship mapping (1:1, 1:N, N:M), and automated schema generation. |
| **Database** | **MySQL 8.0 Community** | ACID-compliant relational database engine supporting foreign keys, transactional integrity with InnoDB, and robust indexing. |
| **Template Engine** | **Thymeleaf 3.x** | Server-side template engine that binds directly with Spring MVC Model attributes, offering clean HTML5 syntax without client-side build pipelines. |
| **Styling & UI** | **Bootstrap 5.3 + Custom CSS** | Mobile-first responsive layout grid, modern UI components (Modals, Toasts, Badges, Cards), and fast client-side rendering. |
| **Build & Tooling** | **Apache Maven & Git** | Standardized dependency management, consistent builds across team environments, and version-controlled Git workflows. |

---

## 13. Agile Product Backlog & User Stories

### Epic 1: Identity, Authentication & Security
- **US-01 (Registration):** *As a new customer*, I want to register an account with my email and personal details so that I can order food.  
  *Acceptance Criteria:* Given valid inputs, when submitted, a customer profile is persisted with a BCrypt-hashed password and a success notification is shown.
- **US-02 (Authentication & Session):** *As a registered customer*, I want to log into the system with my credentials so that I can access my cart and past orders.  
  *Acceptance Criteria:* Valid credentials grant access and route to the menu with active session; invalid credentials display an error alert.

### Epic 2: Menu Catalog & Shopping Cart
- **US-03 (Menu Browsing & Search):** *As a customer*, I want to browse food categories and search for items by keyword/price so that I can quickly choose what to order.  
  *Acceptance Criteria:* Filtering updates the food grid dynamically; items show name, image, description, price, and stock status.
- **US-04 (Cart Management):** *As a customer*, I want to add, update, and remove food items in my cart so that I can review items and totals prior to checkout.  
  *Acceptance Criteria:* Cart automatically updates line-item subtotals, applies tax and delivery charge, and prevents quantities exceeding available inventory.

### Epic 3: Checkout, Payment & Order Lifecycle
- **US-05 (Checkout & Payment):** *As a customer*, I want to provide my delivery address and pay via card so that my order is placed.  
  *Acceptance Criteria:* Payment executes; upon approval, order status becomes `PENDING`, payment is logged as `SUCCESS`, and inventory stock is atomically decremented.
- **US-06 (Order Tracking & Self-Cancellation):** *As a customer*, I want to view my order progress and cancel it if preparation has not yet begun.  
  *Acceptance Criteria:* Status displays progress bar. "Cancel" button is enabled only in `PENDING` or `CONFIRMED` states and restores inventory upon execution.

### Epic 4: Restaurant Back-Office Administration
- **US-07 (Category & Menu Management):** *As an admin*, I want to add, edit, and deactivate food categories and food items so that our catalog is always accurate.  
  *Acceptance Criteria:* Admin can upload image paths, modify base prices, and toggle item availability.
- **US-08 (Inventory & Stock Monitoring):** *As an admin*, I want to monitor inventory counts and restock items so that we avoid running out of stock.  
  *Acceptance Criteria:* Low-stock items (< 10 units) are highlighted in red; admin can increment stock in one click.
- **US-09 (Order Queue & Fulfillment):** *As an admin/kitchen manager*, I want to view active orders and advance their preparation/delivery status.  
  *Acceptance Criteria:* Status dropdown allows advancing orders through `CONFIRMED` $\rightarrow$ `PREPARING` $\rightarrow$ `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED`.

### Epic 5: Financial Analytics & Business Intelligence
- **US-10 (Operating Expense Logging):** *As an admin*, I want to record daily operational expenses so that we can accurately calculate net earnings.  
  *Acceptance Criteria:* Form captures date, expense category (Utilities, Ingredients, Wages), amount, and notes.
- **US-11 (Sales & Profit Reporting):** *As an admin*, I want to generate daily and monthly sales, expense, and profit reports so that I can evaluate restaurant profitability.  
  *Acceptance Criteria:* Dashboard aggregates gross sales, total expenses, and calculates Net Profit with date-filtering controls.

---
