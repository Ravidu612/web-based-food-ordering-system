# Unified Modeling Language (UML) Design Specification
## Project Title: Web-based Food Ordering System
**Course:** SE2030 - Software Engineering  
**Modeling Standard:** OMG UML 2.5 Standard (Rendered via Mermaid)

---

## 1. System Overview Diagram

The System Overview Diagram illustrates the high-level subsystem decomposition, depicting how external human actors (**Customer** and **Administrator**) interact with frontend presentations, security gateways, application business modules, and the persistence tier.

```mermaid
graph TD
    subgraph Actors["Human Actors"]
        Cust["👤 Customer"]
        Admin["👨‍💼 Restaurant Administrator"]
    end

    subgraph PresentationTier["Presentation & Client Layer"]
        CustUI["🖥️ Customer Web Portal (Bootstrap 5 / Thymeleaf)"]
        AdminUI["📊 Admin Back-Office Dashboard (Bootstrap 5 / Thymeleaf)"]
    end

    subgraph SecurityTier["Security & Gateway Layer"]
        AuthFilter["🛡️ Spring Security Filter Chain (RBAC / CSRF / BCrypt)"]
    end

    subgraph BusinessServiceTier["Application Service Layer"]
        AuthSvc["Auth & User Service"]
        CatalogSvc["Food Catalog Service"]
        CartSvc["Cart & Pricing Service"]
        OrderSvc["Order Processing Service"]
        PaymentSvc["Payment Gateway Service"]
        InventorySvc["Inventory & Stock Service"]
        ExpenseSvc["Expense Management Service"]
        ReportSvc["Analytics & Reporting Service"]
    end

    subgraph PersistenceTier["Data Persistence Tier (Spring Data JPA)"]
        Repos["Hibernate ORM & JPA Repositories"]
    end

    subgraph DatabaseTier["Database Tier"]
        DB[("🗄️ MySQL 8.0 Database (InnoDB)")]
    end

    Cust -->|HTTPS / Browsing & Ordering| CustUI
    Admin -->|HTTPS / Administration| AdminUI

    CustUI --> AuthFilter
    AdminUI --> AuthFilter

    AuthFilter --> AuthSvc
    AuthFilter --> CatalogSvc
    AuthFilter --> CartSvc
    AuthFilter --> OrderSvc
    AuthFilter --> PaymentSvc
    AuthFilter --> InventorySvc
    AuthFilter --> ExpenseSvc
    AuthFilter --> ReportSvc

    AuthSvc --> Repos
    CatalogSvc --> Repos
    CartSvc --> Repos
    OrderSvc --> Repos
    PaymentSvc --> Repos
    InventorySvc --> Repos
    ExpenseSvc --> Repos
    ReportSvc --> Repos

    Repos --> DB
```

---

## 2. Use Case Model

### 2.1 Use Case Diagram

```mermaid
graph LR
    Customer["👤 Customer"]
    Admin["👨‍💼 Administrator"]

    subgraph Authentication["Account & Security"]
        UC01["UC-01: Register Customer Account"]
        UC02["UC-02: Authenticate & Login"]
        UC03["UC-03: Logout"]
    end

    subgraph CustomerPortal["Customer Food Ordering"]
        UC04["UC-04: Browse Menu & Filter by Category"]
        UC05["UC-05: Search Food by Keyword"]
        UC06["UC-06: Manage Shopping Cart Items"]
        UC07["UC-07: Checkout & Provide Delivery Address"]
        UC08["UC-08: Process Online Payment"]
        UC09["UC-09: Track Live Order Status"]
        UC10["UC-10: View Historical Orders"]
        UC11["UC-11: Cancel Pending Order"]
    end

    subgraph AdminPortal["Admin Back-Office Management"]
        UC12["UC-12: View Executive KPI Dashboard"]
        UC13["UC-13: Manage Food Categories (CRUD)"]
        UC14["UC-14: Manage Food Items & Pricing (CRUD)"]
        UC15["UC-15: Monitor & Restock Inventory"]
        UC16["UC-16: Dispatch & Advance Order Lifecycle"]
        UC17["UC-17: View Registered Customer Directory"]
        UC18["UC-18: Audit Payment Transactions"]
        UC19["UC-19: Log Operational Expenses"]
        UC20["UC-20: Generate Sales, Expense & Profit Reports"]
    end

    %% Customer Connections
    Customer --> UC01
    Customer --> UC02
    Customer --> UC03
    Customer --> UC04
    Customer --> UC05
    Customer --> UC06
    Customer --> UC07
    Customer --> UC08
    Customer --> UC09
    Customer --> UC10
    Customer --> UC11

    %% Admin Connections
    Admin --> UC02
    Admin --> UC03
    Admin --> UC12
    Admin --> UC13
    Admin --> UC14
    Admin --> UC15
    Admin --> UC16
    Admin --> UC17
    Admin --> UC18
    Admin --> UC19
    Admin --> UC20

    %% Includes & Extends
    UC07 -.->|includes| UC08
    UC06 -.->|includes| UC04
    UC11 -.->|extends| UC09
```

---

### 2.2 Formal Use Case Descriptions

#### Use Case 1: UC-07 & UC-08 (Checkout & Process Online Payment)
- **Primary Actor:** Customer
- **Preconditions:**
  1. Customer is logged in with an active authenticated session.
  2. Customer's shopping cart contains at least one food item.
  3. Requested item quantities do not exceed available inventory stock.
- **Main Success Scenario (Happy Path):**
  1. Customer navigates to `/cart` and clicks **"Proceed to Checkout"**.
  2. System displays the checkout review page with item subtotal, calculated sales tax (5%), delivery fee ($3.50), grand total, and pre-filled delivery address from customer profile.
  3. Customer verifies delivery address, enters optional delivery notes, and inputs card details (Card Number, Expiry, CVV).
  4. Customer clicks **"Confirm & Pay"**.
  5. System validates payment details via simulated gateway service and generates transaction token `TXN-XXXXXXXXXX`.
  6. Within a single transaction (`@Transactional`):
     - Order entity is created in `PENDING` state with a unique `order_number`.
     - Order line items are cloned with snapshot unit prices.
     - Payment record is persisted with status `SUCCESS`.
     - Inventory stock for all ordered foods is decremented.
     - Customer's active cart is cleared.
  7. System redirects customer to the **Order Confirmation & Tracking** page displaying live status `CONFIRMED`.
- **Alternative Flows:**
  - *4a. Insufficient Stock at Checkout:* If stock for any item was claimed by another customer, system aborts checkout, redirects to cart, and alerts customer with the updated available stock.
  - *5a. Payment Gateway Rejection:* If card validation fails (e.g. invalid CVV/expiry), system logs payment as `FAILED`, cancels order creation, does not decrement stock, and displays a descriptive error message on checkout form.
- **Postconditions:**
  - Order is persisted in database with initial status `CONFIRMED`.
  - Payment record is permanently logged.
  - Inventory counts are decremented.
  - Cart is cleared.

---

#### Use Case 2: UC-11 (Cancel Pending Order)
- **Primary Actor:** Customer
- **Preconditions:**
  1. Customer is authenticated and owns the targeted order.
  2. Order's current status is strictly `PENDING` or `CONFIRMED`.
- **Main Success Scenario:**
  1. Customer opens the Order Details page `/orders/{id}`.
  2. Customer clicks **"Cancel Order"** and provides an optional cancellation reason.
  3. System prompts for confirmation via modal dialog. Customer confirms.
  4. System verifies that order status has not advanced to `PREPARING`.
  5. Within a single transaction (`@Transactional`):
     - Order status is updated to `CANCELLED`.
     - Order cancellation timestamp and reason are stored.
     - Each line item's ordered quantity is credited back to the `inventory` table.
  6. System reloads the order page showing badge `CANCELLED` and displays a success alert indicating stock has been restored.
- **Alternative Flows:**
  - *4a. Kitchen Already Began Preparation:* If status changed to `PREPARING`, `OUT_FOR_DELIVERY`, or `DELIVERED`, system rejects cancellation with error message: *"Cancellation is not permitted once food preparation has started. Please contact restaurant support."*
- **Postconditions:**
  - Order status is set to `CANCELLED`.
  - Stock is replenished in the `inventory` table.

---

#### Use Case 3: UC-16 (Dispatch & Advance Order Lifecycle)
- **Primary Actor:** Administrator / Kitchen Dispatcher
- **Preconditions:**
  1. Administrator is authenticated with `ROLE_ADMIN`.
  2. Target order exists in the order management queue.
- **Main Success Scenario:**
  1. Admin opens `/admin/orders` dashboard queue.
  2. Admin selects the active order and chooses the next milestone status from the dropdown:
     - `CONFIRMED` $\rightarrow$ `PREPARING`
     - `PREPARING` $\rightarrow$ `OUT_FOR_DELIVERY`
     - `OUT_FOR_DELIVERY` $\rightarrow$ `DELIVERED`
  3. Admin clicks **"Update Status"**.
  4. System validates the state transition order and updates `order_status` in the database with updated timestamp.
  5. If new status is `DELIVERED`, system creates the associated `sales` ledger entry recording recognized revenue, COGS, and gross margin.
  6. Dashboard updates in real-time.
- **Postconditions:**
  - Order reflects latest operational milestone.
  - On delivery, revenue is recognized in the financial sales ledger.

---

## 3. Activity Diagrams

### 3.1 Customer Checkout & Atomic Payment Activity Diagram

```mermaid
stateDiagram-v2
    [*] --> ViewCart
    ViewCart --> ValidateStock: Click "Proceed to Checkout"
    
    state ValidateStock <<choice>>
    ValidateStock --> DisplayCartError: Stock Insufficient
    DisplayCartError --> ViewCart
    ValidateStock --> FillCheckoutForm: Stock Available

    FillCheckoutForm --> SubmitPayment: Enter Address & Card Details
    
    state ProcessPayment <<choice>>
    SubmitPayment --> ProcessPayment: Send Payment Request
    ProcessPayment --> HandlePaymentFailure: Gateway Returns Error / Invalid Card
    HandlePaymentFailure --> FillCheckoutForm: Display Error Message

    ProcessPayment --> AtomicTransaction: Gateway Returns Success

    state AtomicTransaction {
        [*] --> CreateOrderRecord
        CreateOrderRecord --> CreateOrderItemSnapshots
        CreateOrderItemSnapshots --> LogPaymentSuccess
        LogPaymentSuccess --> DecrementInventory
        DecrementInventory --> ClearCustomerCart
        ClearCustomerCart --> [*]
    }

    AtomicTransaction --> DisplayConfirmation: Transaction Committed
    DisplayConfirmation --> [*]
```

---

### 3.2 Admin Order Fulfillment & Financial Recognition Activity Diagram

```mermaid
stateDiagram-v2
    [*] --> ViewOrderQueue
    ViewOrderQueue --> SelectOrder: Select Incoming Order
    
    state CheckCurrentState <<choice>>
    SelectOrder --> CheckCurrentState: Choose New Status

    CheckCurrentState --> SetPreparing: Advance to "PREPARING"
    SetPreparing --> NotifyKitchen: Kitchen Starts Cooking

    CheckCurrentState --> SetOutForDelivery: Advance to "OUT_FOR_DELIVERY"
    SetOutForDelivery --> NotifyDriver: Dispatch Driver

    CheckCurrentState --> SetDelivered: Advance to "DELIVERED"
    state SetDelivered {
        [*] --> MarkOrderStatusDelivered
        MarkOrderStatusDelivered --> CalculateOrderCOGS
        CalculateOrderCOGS --> InsertSalesLedgerRecord
        InsertSalesLedgerRecord --> [*]
    }

    CheckCurrentState --> AdminCancel: Choose "CANCELLED"
    state AdminCancel {
        [*] --> MarkOrderStatusCancelled
        MarkOrderStatusCancelled --> RestoreInventoryStock
        RestoreInventoryStock --> [*]
    }

    NotifyKitchen --> ViewOrderQueue
    NotifyDriver --> ViewOrderQueue
    SetDelivered --> ViewOrderQueue
    AdminCancel --> ViewOrderQueue
```

---

## 4. Sequence Diagrams

### 4.1 Sequence Diagram: Customer Registration & BCrypt Authentication

```mermaid
sequenceDiagram
    autonumber
    actor Customer as 👤 Customer
    participant RegCtrl as 🌐 AuthController
    participant UserSvc as ⚙️ UserService
    participant PwdEncoder as 🔒 BCryptPasswordEncoder
    participant UserRepo as 💾 UserRepository
    participant CustRepo as 💾 CustomerRepository
    participant DB as 🗄️ MySQL Database

    Customer->>RegCtrl: POST /register (DTO: email, password, name, phone, address)
    activate RegCtrl
    RegCtrl->>UserSvc: registerCustomer(CustomerRegistrationDto)
    activate UserSvc

    UserSvc->>UserRepo: existsByEmail(email)
    activate UserRepo
    UserRepo->>DB: SELECT COUNT(*) FROM users WHERE email = ?
    DB-->>UserRepo: 0 (Available)
    UserRepo-->>UserSvc: false
    deactivate UserRepo

    UserSvc->>PwdEncoder: encode(rawPassword)
    activate PwdEncoder
    PwdEncoder-->>UserSvc: $2a$10$eAccYoNO32OmArPoetdnQ...
    deactivate PwdEncoder

    UserSvc->>UserRepo: save(UserEntity)
    activate UserRepo
    UserRepo->>DB: INSERT INTO users, user_roles
    DB-->>UserRepo: Generated User ID
    UserRepo-->>UserSvc: UserEntity
    deactivate UserRepo

    UserSvc->>CustRepo: save(CustomerEntity)
    activate CustRepo
    CustRepo->>DB: INSERT INTO customers (user_id, first_name, last_name, ...)
    DB-->>CustRepo: Generated Customer ID
    CustRepo-->>UserSvc: CustomerEntity
    deactivate CustRepo

    UserSvc-->>RegCtrl: Registered Customer Profile
    deactivate UserSvc
    RegCtrl-->>Customer: 302 Redirect to /login?success=registered
    deactivate RegCtrl
```

---

### 4.2 Sequence Diagram: Checkout, Atomic Payment & Inventory Decrement

```mermaid
sequenceDiagram
    autonumber
    actor Customer as 👤 Customer
    participant CheckoutCtrl as 🌐 CheckoutController
    participant OrderSvc as ⚙️ OrderService (@Transactional)
    participant InvSvc as ⚙️ InventoryService
    participant PaySvc as ⚙️ PaymentGatewayService
    participant OrderRepo as 💾 OrderRepository
    participant PayRepo as 💾 PaymentRepository
    participant CartSvc as ⚙️ CartService
    participant DB as 🗄️ MySQL Database

    Customer->>CheckoutCtrl: POST /checkout/place-order (Address, Notes, CardDto)
    activate CheckoutCtrl
    CheckoutCtrl->>OrderSvc: processOrderCheckout(customerId, checkoutDto)
    activate OrderSvc

    %% Step 1: Stock Check
    OrderSvc->>InvSvc: verifyStockAvailability(cartItems)
    activate InvSvc
    InvSvc->>DB: SELECT stock_quantity FROM inventory WHERE food_id = ?
    DB-->>InvSvc: Stock Sufficient
    InvSvc-->>OrderSvc: Stock Verified OK
    deactivate InvSvc

    %% Step 2: Payment Gateway Execution
    OrderSvc->>PaySvc: executePayment(totalAmount, cardDto)
    activate PaySvc
    PaySvc-->>OrderSvc: PaymentResult(Status=SUCCESS, TxnId="TXN-998124")
    deactivate PaySvc

    %% Step 3: Atomic Persistence
    OrderSvc->>OrderRepo: save(Order with OrderItems snapshot)
    activate OrderRepo
    OrderRepo->>DB: INSERT INTO orders, INSERT INTO order_items
    DB-->>OrderRepo: Order ID & Order Number "ORD-20260903-1001"
    OrderRepo-->>OrderSvc: Saved Order
    deactivate OrderRepo

    OrderSvc->>PayRepo: save(PaymentEntity)
    activate PayRepo
    PayRepo->>DB: INSERT INTO payments (order_id, txn_id, amount, status)
    DB-->>PayRepo: Payment ID
    PayRepo-->>OrderSvc: Saved Payment
    deactivate PayRepo

    OrderSvc->>InvSvc: decrementStock(cartItems)
    activate InvSvc
    InvSvc->>DB: UPDATE inventory SET stock_quantity = stock_quantity - ? WHERE food_id = ?
    DB-->>InvSvc: Updated Rows
    InvSvc-->>OrderSvc: Stock Decremented OK
    deactivate InvSvc

    OrderSvc->>CartSvc: clearCart(customerId)
    activate CartSvc
    CartSvc->>DB: DELETE FROM cart_items WHERE cart_id = ?
    DB-->>CartSvc: Cart Cleared
    CartSvc-->>OrderSvc: Done
    deactivate CartSvc

    OrderSvc-->>CheckoutCtrl: OrderConfirmationDto
    deactivate OrderSvc
    CheckoutCtrl-->>Customer: 302 Redirect to /orders/confirmation/{id}
    deactivate CheckoutCtrl
```

---

### 4.3 Sequence Diagram: Order Cancellation & Inventory Stock Restoration

```mermaid
sequenceDiagram
    autonumber
    actor Customer as 👤 Customer
    participant OrderCtrl as 🌐 OrderController
    participant OrderSvc as ⚙️ OrderService (@Transactional)
    participant OrderRepo as 💾 OrderRepository
    participant InvSvc as ⚙️ InventoryService
    participant DB as 🗄️ MySQL Database

    Customer->>OrderCtrl: POST /orders/{id}/cancel (reason)
    activate OrderCtrl
    OrderCtrl->>OrderSvc: cancelOrder(orderId, customerId, reason)
    activate OrderSvc

    OrderSvc->>OrderRepo: findById(orderId)
    activate OrderRepo
    OrderRepo->>DB: SELECT * FROM orders WHERE id = ?
    DB-->>OrderRepo: OrderEntity (status='CONFIRMED')
    OrderRepo-->>OrderSvc: OrderEntity
    deactivate OrderRepo

    %% Validation of State
    alt Order Status is PENDING or CONFIRMED
        OrderSvc->>OrderSvc: setOrderStatus(CANCELLED), setReason(reason)
        OrderSvc->>OrderRepo: save(OrderEntity)
        activate OrderRepo
        OrderRepo->>DB: UPDATE orders SET order_status='CANCELLED' WHERE id = ?
        DB-->>OrderRepo: Success
        OrderRepo-->>OrderSvc: Updated Order
        deactivate OrderRepo

        OrderSvc->>InvSvc: restoreStock(order.getOrderItems())
        activate InvSvc
        InvSvc->>DB: UPDATE inventory SET stock_quantity = stock_quantity + ? WHERE food_id = ?
        DB-->>InvSvc: Stock Restored
        InvSvc-->>OrderSvc: Restock Complete
        deactivate InvSvc

        OrderSvc-->>OrderCtrl: CancellationSuccess
        OrderCtrl-->>Customer: 302 Redirect /orders/{id}?cancelled=true
    else Order Status is PREPARING, OUT_FOR_DELIVERY, or DELIVERED
        OrderSvc-->>OrderCtrl: throw IllegalOrderStateException("Cannot cancel order in prep")
        OrderCtrl-->>Customer: 400 Bad Request Alert ("Order preparation already started")
    end
    deactivate OrderSvc
    deactivate OrderCtrl
```

---

## 5. Domain Class Diagram (OOP & Spring Boot Entities)

```mermaid
classDiagram
    direction TB

    class User {
        -Long id
        -String email
        -String password
        -Boolean isEnabled
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Set~Role~ roles
        +hasRole(String roleName) Boolean
    }

    class Role {
        -Long id
        -String name
        -String description
    }

    class Customer {
        -Long id
        -User user
        -String firstName
        -String lastName
        -String phoneNumber
        -String defaultDeliveryAddress
        -String city
        -String postalCode
        -Cart cart
        -List~Order~ orders
        +getFullName() String
    }

    class Admin {
        -Long id
        -User user
        -String fullName
        -String designation
        -String phoneNumber
        -List~Expense~ recordedExpenses
        -List~Report~ generatedReports
    }

    class FoodCategory {
        -Long id
        -String name
        -String description
        -String imageUrl
        -Boolean isActive
        -List~Food~ foods
    }

    class Food {
        -Long id
        -FoodCategory category
        -String name
        -String description
        -BigDecimal price
        -BigDecimal costPrice
        -Integer prepTimeMinutes
        -String imageUrl
        -Boolean isAvailable
        -Boolean isPromotional
        -BigDecimal discountPercentage
        -Inventory inventory
        +getEffectivePrice() BigDecimal
    }

    class Inventory {
        -Long id
        -Food food
        -Integer stockQuantity
        -Integer lowStockThreshold
        -LocalDateTime lastRestockedAt
        +isLowStock() Boolean
        +hasSufficientStock(int requestedQty) Boolean
    }

    class Cart {
        -Long id
        -Customer customer
        -String sessionToken
        -BigDecimal totalAmount
        -List~CartItem~ cartItems
        +recalculateTotal() BigDecimal
    }

    class CartItem {
        -Long id
        -Cart cart
        -Food food
        -Integer quantity
        -BigDecimal unitPrice
        -BigDecimal subtotal
        +calculateSubtotal() BigDecimal
    }

    class Order {
        -Long id
        -String orderNumber
        -Customer customer
        -OrderStatus orderStatus
        -BigDecimal subtotalAmount
        -BigDecimal taxAmount
        -BigDecimal deliveryFee
        -BigDecimal totalAmount
        -String deliveryAddress
        -String contactPhone
        -String deliveryNotes
        -String cancellationReason
        -LocalDateTime orderDate
        -List~OrderItem~ orderItems
        -Payment payment
        -Sale sale
        +canBeCancelled() Boolean
    }

    class OrderItem {
        -Long id
        -Order order
        -Food food
        -String foodName
        -BigDecimal unitPrice
        -Integer quantity
        -BigDecimal subtotal
    }

    class Payment {
        -Long id
        -Order order
        -String transactionId
        -String paymentMethod
        -BigDecimal amount
        -PaymentStatus paymentStatus
        -String cardLastFour
        -String responseCode
        -LocalDateTime paymentDate
    }

    class Expense {
        -Long id
        -Admin recordedByAdmin
        -String expenseCategory
        -String title
        -BigDecimal amount
        -LocalDate expenseDate
        -String description
    }

    class Sale {
        -Long id
        -Order order
        -BigDecimal totalRevenue
        -BigDecimal totalCogs
        -BigDecimal netMargin
        -LocalDate saleDate
    }

    class Report {
        -Long id
        -Admin generatedByAdmin
        -String reportType
        -String reportTitle
        -LocalDate startDate
        -LocalDate endDate
        -BigDecimal totalRevenue
        -BigDecimal totalExpenses
        -BigDecimal netProfit
        -String reportDataJson
        -LocalDateTime generatedAt
    }

    %% Relationships
    User "1" *-- "many" Role : assigned via user_roles
    Customer "1" *-- "1" User : maps to
    Admin "1" *-- "1" User : maps to
    FoodCategory "1" *-- "many" Food : categorizes
    Food "1" *-- "1" Inventory : tracks stock
    Customer "1" *-- "1" Cart : owns
    Cart "1" *-- "many" CartItem : contains
    Food "1" <-- "many" CartItem : references
    Customer "1" *-- "many" Order : places
    Order "1" *-- "many" OrderItem : contains
    Food "1" <-- "many" OrderItem : references snapshot
    Order "1" *-- "1" Payment : settled by
    Order "1" *-- "1" Sale : creates
    Admin "1" *-- "many" Expense : logs
    Admin "1" *-- "many" Report : generates
```

---

## 6. Visual Entity-Relationship Diagram (Crow's Foot ERD)

```mermaid
erDiagram
    USERS ||--|{ USER_ROLES : "assigned"
    ROLES ||--|{ USER_ROLES : "contained_in"
    USERS ||--|| CUSTOMERS : "authenticates"
    USERS ||--|| ADMINS : "authenticates"

    FOOD_CATEGORIES ||--|{ FOODS : "contains"
    FOODS ||--|| INVENTORY : "tracked_by"

    CUSTOMERS ||--|| CARTS : "owns"
    CARTS ||--|{ CART_ITEMS : "has"
    FOODS ||--|{ CART_ITEMS : "added_as"

    CUSTOMERS ||--|{ ORDERS : "places"
    ORDERS ||--|{ ORDER_ITEMS : "includes"
    FOODS ||--|{ ORDER_ITEMS : "ordered_in"

    ORDERS ||--|| PAYMENTS : "paid_with"
    ORDERS ||--|| SALES : "yields"

    ADMINS ||--|{ EXPENSES : "records"
    ADMINS ||--|{ REPORTS : "generates"

    USERS {
        bigint id PK
        varchar email UK
        varchar password
        boolean is_enabled
        timestamp created_at
        timestamp updated_at
    }

    ROLES {
        bigint id PK
        varchar name UK
        varchar description
    }

    USER_ROLES {
        bigint user_id PK,FK
        bigint role_id PK,FK
    }

    CUSTOMERS {
        bigint id PK
        bigint user_id FK,UK
        varchar first_name
        varchar last_name
        varchar phone_number
        text default_delivery_address
        varchar city
        varchar postal_code
    }

    ADMINS {
        bigint id PK
        bigint user_id FK,UK
        varchar full_name
        varchar designation
        varchar phone_number
    }

    FOOD_CATEGORIES {
        bigint id PK
        varchar name UK
        text description
        varchar image_url
        boolean is_active
    }

    FOODS {
        bigint id PK
        bigint category_id FK
        varchar name
        text description
        decimal price
        decimal cost_price
        int prep_time_minutes
        varchar image_url
        boolean is_available
        boolean is_promotional
        decimal discount_percentage
    }

    INVENTORY {
        bigint id PK
        bigint food_id FK,UK
        int stock_quantity
        int low_stock_threshold
        timestamp last_restocked_at
    }

    CARTS {
        bigint id PK
        bigint customer_id FK,UK
        varchar session_token
        decimal total_amount
    }

    CART_ITEMS {
        bigint id PK
        bigint cart_id FK
        bigint food_id FK
        int quantity
        decimal unit_price
        decimal subtotal
    }

    ORDERS {
        bigint id PK
        varchar order_number UK
        bigint customer_id FK
        varchar order_status
        decimal subtotal_amount
        decimal tax_amount
        decimal delivery_fee
        decimal total_amount
        text delivery_address
        varchar contact_phone
        text delivery_notes
        varchar cancellation_reason
        timestamp order_date
    }

    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK
        bigint food_id FK
        varchar food_name
        decimal unit_price
        int quantity
        decimal subtotal
    }

    PAYMENTS {
        bigint id PK
        bigint order_id FK,UK
        varchar transaction_id UK
        varchar payment_method
        decimal amount
        varchar payment_status
        varchar card_last_four
        varchar response_code
        timestamp payment_date
    }

    EXPENSES {
        bigint id PK
        bigint recorded_by_admin_id FK
        varchar expense_category
        varchar title
        decimal amount
        date expense_date
        text description
    }

    SALES {
        bigint id PK
        bigint order_id FK,UK
        decimal total_revenue
        decimal total_cogs
        decimal net_margin
        date sale_date
    }

    REPORTS {
        bigint id PK
        bigint generated_by_admin_id FK
        varchar report_type
        varchar report_title
        date start_date
        date end_date
        decimal total_revenue
        decimal total_expenses
        decimal net_profit
        longtext report_data_json
        timestamp generated_at
    }
```

---

## 7. Package Diagram (Clean Layered Architecture)

The Package Diagram illustrates the decoupling of layers following Spring Boot enterprise conventions and Clean Architecture.

```mermaid
graph TD
    subgraph com.foodie.app["Root: com.foodie.app"]
        subgraph config["📁 config"]
            SecConfig["SecurityConfig"]
            WebConfig["WebMvcConfig"]
            JPAConfig["JpaAuditingConfig"]
        end

        subgraph controller["📁 controller"]
            AuthCtrl["AuthController"]
            CustCtrl["CustomerController"]
            MenuCtrl["MenuController"]
            CartCtrl["CartController"]
            OrderCtrl["OrderController"]
            AdminCtrl["AdminDashboardController"]
            AdminFoodCtrl["AdminFoodController"]
            AdminOrderCtrl["AdminOrderController"]
            AdminReportCtrl["AdminReportController"]
        end

        subgraph api["📁 controller.api (REST Endpoints)"]
            AuthApi["AuthRestController"]
            FoodApi["FoodRestController"]
            CartApi["CartRestController"]
            OrderApi["OrderRestController"]
            InventoryApi["InventoryRestController"]
            AnalyticsApi["AnalyticsRestController"]
        end

        subgraph dto["📁 dto"]
            AuthDTOs["Auth & Register DTOs"]
            CartDTOs["CartItem & Cart DTOs"]
            CheckoutDTOs["Checkout & Payment DTOs"]
            OrderDTOs["OrderResponse DTOs"]
            AdminDTOs["Expense & Report DTOs"]
        end

        subgraph service["📁 service"]
            subgraph svc_interfaces["service interfaces"]
                IUserSvc["UserService"]
                IFoodSvc["FoodService"]
                ICartSvc["CartService"]
                IOrderSvc["OrderService"]
                IPaymentSvc["PaymentService"]
                IInventorySvc["InventoryService"]
                IExpenseSvc["ExpenseService"]
                IReportSvc["ReportService"]
            end
            subgraph svc_impl["service.impl"]
                UserSvcImpl["UserServiceImpl"]
                FoodSvcImpl["FoodServiceImpl"]
                CartSvcImpl["CartServiceImpl"]
                OrderSvcImpl["OrderServiceImpl"]
                PaymentSvcImpl["PaymentServiceImpl"]
                InventorySvcImpl["InventoryServiceImpl"]
                ExpenseSvcImpl["ExpenseServiceImpl"]
                ReportSvcImpl["ReportServiceImpl"]
            end
        end

        subgraph repository["📁 repository"]
            UserRepo["UserRepository"]
            RoleRepo["RoleRepository"]
            CustRepo["CustomerRepository"]
            AdminRepo["AdminRepository"]
            CategoryRepo["FoodCategoryRepository"]
            FoodRepo["FoodRepository"]
            InventoryRepo["InventoryRepository"]
            CartRepo["CartRepository"]
            CartItemRepo["CartItemRepository"]
            OrderRepo["OrderRepository"]
            OrderItemRepo["OrderItemRepository"]
            PaymentRepo["PaymentRepository"]
            ExpenseRepo["ExpenseRepository"]
            SaleRepo["SaleRepository"]
            ReportRepo["ReportRepository"]
        end

        subgraph entity["📁 entity"]
            UserE["User"]
            RoleE["Role"]
            CustE["Customer"]
            AdminE["Admin"]
            CatE["FoodCategory"]
            FoodE["Food"]
            InvE["Inventory"]
            CartE["Cart"]
            CartItemE["CartItem"]
            OrderE["Order"]
            OrderItemE["OrderItem"]
            PaymentE["Payment"]
            ExpenseE["Expense"]
            SaleE["Sale"]
            ReportE["Report"]
        end

        subgraph exception["📁 exception"]
            GlobalExceptionHandler["GlobalExceptionHandler"]
            ResourceNotFoundEx["ResourceNotFoundException"]
            InsufficientStockEx["InsufficientStockException"]
            InvalidOrderStateEx["InvalidOrderStateException"]
            PaymentFailedEx["PaymentFailedException"]
        end
    end

    %% Dependency flow
    controller --> service
    api --> service
    controller --> dto
    api --> dto
    service --> repository
    service --> dto
    repository --> entity
    config --> service
```

---
