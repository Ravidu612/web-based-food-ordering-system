# Backend Implementation & Codebase Architecture Specification
## Project Title: Web-based Food Ordering System
**Course:** SE2030 - Software Engineering  
**Language & Runtime:** Java 21 LTS  
**Frameworks:** Spring Boot 3.3.3 • Spring Security 6 • Spring Data JPA • Hibernate ORM

---

## 1. Backend Architecture Overview

The backend is structured according to **Clean Architecture** and the **Multi-Tier Model-View-Controller (MVC)** design pattern. It provides both server-rendered Thymeleaf views and RESTful JSON APIs (`/api/v1/**`).

### 1.1 Complete Package Structure
```
src/main/java/com/foodie/app/
├── FoodOrderingApplication.java         # Spring Boot Application Entry Point
├── config/                              # Configuration & Security Beans
│   ├── SecurityConfig.java              # Spring Security 6 Filter Chain & RBAC
│   ├── CustomAuthenticationSuccessHandler.java # Role-based portal redirector
│   └── WebMvcConfig.java                # MVC View Controllers & Static Mappings
├── controller/                          # Server-Side Web MVC Controllers
│   ├── AuthController.java              # Customer Registration & Login routing
│   ├── HomeController.java              # Landing page, Featured & Promo dishes
│   ├── MenuController.java              # Catalog browsing, Category & Keyword filter
│   ├── CartController.java              # Cart management & quantity updates
│   ├── CheckoutController.java          # Address capture & simulated payment checkout
│   ├── OrderController.java             # Order history, tracking & policy cancellation
│   ├── AdminDashboardController.java    # Executive KPI cards & Customer registry
│   ├── AdminFoodController.java         # Food Categories & Food items CRUD
│   ├── AdminOrderController.java        # Kitchen Order Queue & Status transitions
│   ├── AdminInventoryController.java    # Real-time stock tracking & Restock triggers
│   └── AdminReportController.java       # Sales, Expenses, Net Profit reports
├── controller/api/                      # Stateless RESTful API Controllers
│   ├── AuthRestController.java          # JSON Registration & Login endpoints
│   ├── FoodRestController.java          # Public food catalog & Admin item endpoints
│   ├── CartRestController.java          # Dynamic AJAX Cart CRUD endpoints
│   ├── OrderRestController.java         # Atomic checkout, cancel & tracking APIs
│   └── AdminRestController.java         # Inventory, Expenses & Analytics APIs
├── dto/                                 # Data Transfer Objects & Command Payloads
│   ├── CustomerRegistrationDto.java     # Jakarta Bean Validated registration form
│   ├── LoginRequestDto.java             # Authentication request model
│   ├── FoodDto.java                     # Food item projection with stock info
│   ├── FoodCategoryDto.java             # Category projection with dish counts
│   ├── AddToCartDto.java                # Add-to-cart command object
│   ├── UpdateCartItemDto.java           # Cart quantity modification command
│   ├── CartItemDto.java                 # Line-item projection
│   ├── CartDto.java                     # Cart aggregation with subtotal, tax & total
│   ├── CheckoutDto.java                 # Order delivery & card payment model
│   ├── OrderItemDto.java                # Snapshot order item projection
│   ├── OrderResponseDto.java            # Comprehensive order projection
│   ├── CancelOrderDto.java              # Order cancellation command
│   ├── UpdateOrderStatusDto.java        # Admin status advance command
│   ├── ExpenseDto.java                  # Expense record command & projection
│   ├── RestockDto.java                  # Inventory replenishment command
│   ├── SalesReportDto.java              # Sales revenue aggregation projection
│   ├── ProfitReportDto.java             # Profit & Loss financial projection
│   └── ApiResponse.java                 # Generic JSON response envelope
├── entity/                              # JPA Domain Entities (16 Relational Tables)
│   ├── User.java                        # User security credentials
│   ├── Role.java                        # System roles (ROLE_CUSTOMER, ROLE_ADMIN)
│   ├── Customer.java                    # Customer demographic profile
│   ├── Admin.java                       # Administrator staff profile
│   ├── FoodCategory.java                # Menu catalog categories
│   ├── Food.java                        # Food items with price & prep times
│   ├── Inventory.java                   # Real-time stock counts & thresholds
│   ├── Cart.java                        # Active shopping cart header
│   ├── CartItem.java                    # Cart line-items
│   ├── Order.java                       # Master order entity
│   ├── OrderItem.java                   # Immutable snapshot line items
│   ├── Payment.java                     # Payment transaction audit ledger
│   ├── Expense.java                     # Operating expenditures ledger
│   ├── Sale.java                        # Financial sales & margin ledger
│   ├── Report.java                      # Precomputed business reports
│   └── enums/                           # Domain Enums
│       ├── OrderStatus.java             # PENDING, CONFIRMED, PREPARING, etc.
│       ├── PaymentStatus.java           # SUCCESS, FAILED, REFUNDED
│       ├── ExpenseCategory.java         # RAW_INGREDIENTS, UTILITIES, etc.
│       └── ReportType.java              # DAILY_SALES, MONTHLY_PROFIT, etc.
├── exception/                           # Exception Hierarchy & Global Handlers
│   ├── FoodOrderingException.java       # Root unchecked business exception
│   ├── ResourceNotFoundException.java   # HTTP 404 Entity not found
│   ├── InsufficientStockException.java  # HTTP 409 Out-of-stock guard
│   ├── InvalidOrderStateException.java  # HTTP 400 Illegal state transition
│   ├── PaymentFailedException.java      # HTTP 402 Card rejection
│   ├── DuplicateResourceException.java  # HTTP 409 Duplicate email
│   ├── UnauthorizedAccessException.java # HTTP 403 Forbidden resource access
│   ├── GlobalExceptionHandler.java      # MVC view error interceptor
│   └── GlobalRestExceptionHandler.java  # REST JSON RFC-7807 error formatter
├── repository/                          # Spring Data JPA Repositories
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── CustomerRepository.java
│   ├── AdminRepository.java
│   ├── FoodCategoryRepository.java
│   ├── FoodRepository.java
│   ├── InventoryRepository.java
│   ├── CartRepository.java
│   ├── CartItemRepository.java
│   ├── OrderRepository.java
│   ├── OrderItemRepository.java
│   ├── PaymentRepository.java
│   ├── ExpenseRepository.java
│   ├── SaleRepository.java
│   └── ReportRepository.java
└── service/                             # Business Logic & Transaction Layer
    ├── UserService.java & UserServiceImpl.java
    ├── FoodCategoryService.java & FoodCategoryServiceImpl.java
    ├── FoodService.java & FoodServiceImpl.java
    ├── InventoryService.java & InventoryServiceImpl.java
    ├── CartService.java & CartServiceImpl.java
    ├── PaymentService.java & PaymentServiceImpl.java
    ├── OrderService.java & OrderServiceImpl.java
    ├── ExpenseService.java & ExpenseServiceImpl.java
    ├── ReportService.java & ReportServiceImpl.java
    └── impl/CustomUserDetailsService.java
```

---

## 2. Key Service Implementations & Transaction Boundaries

### 2.1 Atomic Checkout Execution (`OrderServiceImpl.java`)
1. **Stock Validation:** Interrogates `InventoryRepository` for every line item in the customer's cart.
2. **Snapshot Cloning:** Converts dynamic cart items to immutable `OrderItem` records capturing historical unit prices and food names.
3. **Inventory Decrement:** Atomically reduces `stock_quantity` in the `Inventory` table.
4. **Payment Gateway Settlement:** Calls `PaymentService.processPayment()` to record an immutable transaction record with status `SUCCESS`.
5. **Sales Ledger Entry:** Creates a `Sale` entity recording gross revenue, COGS, and profit margin.
6. **Cart Invalidation:** Purges all items from the customer's active cart.
7. **Rollback Policy:** Any `RuntimeException` triggers a total rollback of database changes.

### 2.2 Order Cancellation & Restocking Policy (`OrderServiceImpl.java`)
- Validates that the order state is currently `PENDING` or `CONFIRMED`.
- If the kitchen has advanced the state to `PREPARING` or beyond, throws `InvalidOrderStateException`.
- Upon successful cancellation, iterates through each `OrderItem` and credits the ordered quantity back to `Inventory.stockQuantity`.

---
