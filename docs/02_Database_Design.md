# Database Design Specification & Data Dictionary
## Project Title: Web-based Food Ordering System
**Course:** SE2030 - Software Engineering  
**Database Engine:** MySQL 8.0 Community Server (InnoDB Engine)  
**Character Set:** `utf8mb4` | **Collation:** `utf8mb4_unicode_ci`

---

## 1. Database Overview & Architecture

The database architecture for the **Web-based Food Ordering System** is designed using strict **Third Normal Form (3NF)** principles to eliminate data redundancy, enforce referential integrity, guarantee ACID compliance for financial and inventory transactions, and support high-throughput relational querying.

### 1.1 Core Design Highlights:
- **Storage Engine:** MySQL **InnoDB** for foreign key enforcement, row-level locking, and crash recovery.
- **Financial Precision:** All monetary fields (`price`, `subtotal`, `tax_amount`, `delivery_fee`, `total_amount`, `amount`, `total_revenue`, `total_expenses`, `net_profit`) are defined as `DECIMAL(10,2)` to avoid IEEE 754 floating-point rounding errors.
- **Snapshot Pricing Integrity:** The `order_items` table stores snapshot copies of `food_name` and `unit_price` at the time of purchase so historical order receipts remain unchanged even if the restaurant subsequently updates current menu prices.
- **Authentication & Security:** Passwords are stored in a `VARCHAR(255)` field to accommodate 60-character standard BCrypt hash digests. Role-based access control is supported via a flexible `users`, `roles`, and `user_roles` normalized mapping.

---

## 2. Normalization Analysis (1NF $\rightarrow$ 2NF $\rightarrow$ 3NF)

1. **First Normal Form (1NF):**
   - All attributes contain only atomic, non-divisible values (e.g. separate columns for `first_name`, `last_name`, `phone_number`).
   - Every table possesses an explicit, unique Primary Key (`id` as `BIGINT AUTO_INCREMENT`).
   - Repeating groups are eliminated; cart line-items and order line-items are stored in dedicated child tables (`cart_items`, `order_items`).

2. **Second Normal Form (2NF):**
   - All tables are in 1NF and every non-key attribute is fully functionally dependent on the entire primary key (no partial dependencies on composite keys).

3. **Third Normal Form (3NF):**
   - All tables are in 2NF and no transitive dependencies exist (non-key attributes do not depend on other non-key attributes). For example, `orders` references `customer_id`, while customer demographic data resides strictly in `customers` / `users`.

---

## 3. Entity Relational Model & Cardinality Mapping

| Primary Entity (1) | Related Entity (N) | Relationship Type | Cardinality | Business Meaning / FK Constraint |
| :--- | :--- | :--- | :--- | :--- |
| `users` | `roles` | Many-to-Many | $M:N$ | Managed via `user_roles` bridge table (`user_id`, `role_id`). |
| `users` | `customers` | One-to-One | $1:1$ | Each customer record maps to exactly one authentication user account (`user_id`). |
| `users` | `admins` | One-to-One | $1:1$ | Each administrator record maps to exactly one authentication user account (`user_id`). |
| `food_categories` | `foods` | One-to-Many | $1:N$ | A category contains multiple food items; a food item belongs to one category (`category_id`). |
| `foods` | `inventory` | One-to-One | $1:1$ | Each distinct food item has one corresponding inventory tracking record (`food_id`). |
| `customers` | `carts` | One-to-One | $1:1$ | Each customer possesses one active shopping cart (`customer_id`). |
| `carts` | `cart_items` | One-to-Many | $1:N$ | A shopping cart contains zero or more line items (`cart_id`). |
| `foods` | `cart_items` | One-to-Many | $1:N$ | A food item can appear as a line item in multiple carts (`food_id`). |
| `customers` | `orders` | One-to-Many | $1:N$ | A customer can place multiple historical orders (`customer_id`). |
| `orders` | `order_items` | One-to-Many | $1:N$ | An order contains one or more immutable order line items (`order_id`). |
| `foods` | `order_items` | One-to-Many | $1:N$ | A food item can be referenced in multiple past order lines (`food_id`). |
| `orders` | `payments` | One-to-One | $1:1$ | Each confirmed order possesses a corresponding payment audit record (`order_id`). |
| `orders` | `sales` | One-to-One | $1:1$ | Each completed order produces one financial sales ledger record (`order_id`). |
| `admins` | `expenses` | One-to-Many | $1:N$ | An admin logs multiple operational expense vouchers (`recorded_by_admin_id`). |
| `admins` | `reports` | One-to-Many | $1:N$ | An admin generates analytical business reports (`generated_by_admin_id`). |

---

## 4. Comprehensive Data Dictionary

### Table 1: `users` (Core Authentication Credentials)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique internal user identifier. |
| `email` | `VARCHAR(150)` | `NOT NULL`, `UNIQUE` | User login email address. |
| `password` | `VARCHAR(255)` | `NOT NULL` | BCrypt-hashed password string. |
| `is_enabled` | `BOOLEAN` | `NOT NULL`, `DEFAULT TRUE` | Account active / enabled flag. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Account creation timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last profile update timestamp. |

### Table 2: `roles` (Security Roles)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique role identifier. |
| `name` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | Security role name (`ROLE_CUSTOMER`, `ROLE_ADMIN`). |
| `description` | `VARCHAR(255)` | `NULL` | Human-readable role description. |

### Table 3: `user_roles` (User-Role Many-to-Many Join Table)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `user_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `users(id)` `ON DELETE CASCADE` | Foreign key referencing user. |
| `role_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `roles(id)` `ON DELETE CASCADE` | Foreign key referencing role. |
| *Composite PK* | `(user_id, role_id)` | `PRIMARY KEY` | Ensures unique role assignment per user. |

### Table 4: `customers` (Customer Demographic Profiles)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique customer profile identifier. |
| `user_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `users(id)` `ON DELETE CASCADE` | 1:1 mapping to authentication record. |
| `first_name` | `VARCHAR(100)` | `NOT NULL` | Customer's first name. |
| `last_name` | `VARCHAR(100)` | `NOT NULL` | Customer's last name. |
| `phone_number` | `VARCHAR(20)` | `NOT NULL` | Contact mobile number. |
| `default_delivery_address` | `TEXT` | `NOT NULL` | Primary street delivery address. |
| `city` | `VARCHAR(100)` | `NOT NULL` | City of residence. |
| `postal_code` | `VARCHAR(20)` | `NULL` | Postal/ZIP code. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Profile creation timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last profile update timestamp. |

### Table 5: `admins` (Administrator Profiles)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique admin identifier. |
| `user_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `users(id)` `ON DELETE CASCADE` | 1:1 mapping to authentication record. |
| `full_name` | `VARCHAR(150)` | `NOT NULL` | Administrator's full legal name. |
| `designation` | `VARCHAR(100)` | `NOT NULL` | Staff title (e.g., "Store Manager", "Head Chef"). |
| `phone_number` | `VARCHAR(20)` | `NOT NULL` | Staff direct contact line. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Staff creation timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last record update timestamp. |

### Table 6: `food_categories` (Menu Categories)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique category identifier. |
| `name` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | Category title (e.g., "Burgers", "Pizzas", "Beverages"). |
| `description` | `TEXT` | `NULL` | Detailed category description. |
| `image_url` | `VARCHAR(255)` | `NULL` | Category banner / icon asset path. |
| `is_active` | `BOOLEAN` | `NOT NULL`, `DEFAULT TRUE` | Visibility flag for customer catalog. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Record creation timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last modification timestamp. |

### Table 7: `foods` (Menu Items Catalog)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique food item identifier. |
| `category_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `food_categories(id)` `ON DELETE RESTRICT` | Associated category foreign key. |
| `name` | `VARCHAR(150)` | `NOT NULL` | Food dish name. |
| `description` | `TEXT` | `NOT NULL` | Ingredients and culinary description. |
| `price` | `DECIMAL(10,2)` | `NOT NULL` | Standard selling price per unit. |
| `cost_price` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Estimated cost of preparation (COGS). |
| `prep_time_minutes` | `INT` | `NOT NULL`, `DEFAULT 15` | Approximate kitchen preparation time. |
| `image_url` | `VARCHAR(255)` | `NULL` | High-resolution dish image URL. |
| `is_available` | `BOOLEAN` | `NOT NULL`, `DEFAULT TRUE` | Active availability toggle. |
| `is_promotional` | `BOOLEAN` | `NOT NULL`, `DEFAULT FALSE` | Special promotional item flag. |
| `discount_percentage`| `DECIMAL(5,2)` | `NOT NULL`, `DEFAULT 0.00` | Active discount rate (0.00 to 100.00). |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Item registration timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last price/content update timestamp. |

### Table 8: `inventory` (Real-Time Stock & Ingredient Quantities)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique inventory record identifier. |
| `food_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `foods(id)` `ON DELETE CASCADE` | 1:1 foreign key referencing food dish. |
| `stock_quantity` | `INT` | `NOT NULL`, `DEFAULT 0` | Current available units in stock. |
| `low_stock_threshold`| `INT`| `NOT NULL`, `DEFAULT 10` | Threshold triggering low-stock alert. |
| `last_restocked_at` | `TIMESTAMP` | `NULL` | Timestamp of most recent restocking action. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last inventory adjustment timestamp. |

### Table 9: `carts` (Shopping Cart Header)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique cart identifier. |
| `customer_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `customers(id)` `ON DELETE CASCADE`| 1:1 mapping to customer. |
| `session_token` | `VARCHAR(100)` | `NULL` | Temporary anonymous session tracker. |
| `total_amount` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Aggregated line items total. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last cart mutation timestamp. |

### Table 10: `cart_items` (Shopping Cart Line Items)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique line item identifier. |
| `cart_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `carts(id)` `ON DELETE CASCADE` | Parent cart foreign key. |
| `food_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `foods(id)` `ON DELETE CASCADE` | Target food item foreign key. |
| `quantity` | `INT` | `NOT NULL`, `CHECK (quantity > 0)` | Number of units added. |
| `unit_price` | `DECIMAL(10,2)` | `NOT NULL` | Base price per unit at addition. |
| `subtotal` | `DECIMAL(10,2)` | `NOT NULL` | Line total (`quantity * unit_price`). |

### Table 11: `orders` (Order Master Entity)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique order entity identifier. |
| `order_number` | `VARCHAR(50)` | `NOT NULL`, `UNIQUE` | Business order reference (e.g., `ORD-20260903-1001`). |
| `customer_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `customers(id)` `ON DELETE RESTRICT` | Customer who placed order. |
| `order_status` | `VARCHAR(30)` | `NOT NULL`, `DEFAULT 'PENDING'` | State (`PENDING`, `CONFIRMED`, `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`). |
| `subtotal_amount`| `DECIMAL(10,2)` | `NOT NULL` | Net food items subtotal. |
| `tax_amount` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Computed sales tax (e.g. 5%). |
| `delivery_fee` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Standard flat delivery fee. |
| `total_amount` | `DECIMAL(10,2)` | `NOT NULL` | Grand total payable. |
| `delivery_address`| `TEXT` | `NOT NULL` | Destination address for dispatch. |
| `contact_phone` | `VARCHAR(20)` | `NOT NULL` | Delivery contact phone number. |
| `delivery_notes` | `TEXT` | `NULL` | Special delivery instructions. |
| `cancellation_reason` | `VARCHAR(255)` | `NULL` | Reason if order is cancelled. |
| `order_date` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Order placement timestamp. |
| `updated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP` | Last status transition timestamp. |

### Table 12: `order_items` (Order Detail Line Items - Snapshot Model)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique order item identifier. |
| `order_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `orders(id)` `ON DELETE CASCADE` | Parent order reference. |
| `food_id` | `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `foods(id)` `ON DELETE RESTRICT` | Reference to catalog food entity. |
| `food_name` | `VARCHAR(150)` | `NOT NULL` | **Immutable snapshot** of food name. |
| `unit_price` | `DECIMAL(10,2)` | `NOT NULL` | **Immutable snapshot** of unit price. |
| `quantity` | `INT` | `NOT NULL`, `CHECK (quantity > 0)` | Ordered quantity. |
| `subtotal` | `DECIMAL(10,2)` | `NOT NULL` | Line total (`unit_price * quantity`). |

### Table 13: `payments` (Payment Transaction Audit Ledger)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique payment identifier. |
| `order_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `orders(id)` `ON DELETE RESTRICT` | Associated order identifier. |
| `transaction_id` | `VARCHAR(100)` | `NOT NULL`, `UNIQUE` | Gateway reference (e.g. `TXN-884920489`). |
| `payment_method` | `VARCHAR(50)` | `NOT NULL` | `CARD`, `ONLINE_BANKING`, `SIMULATED_GATEWAY`. |
| `amount` | `DECIMAL(10,2)` | `NOT NULL` | Exact transaction amount paid. |
| `payment_status` | `VARCHAR(30)` | `NOT NULL` | `SUCCESS`, `FAILED`, `REFUNDED`. |
| `card_last_four` | `VARCHAR(4)` | `NULL` | Masked card digits for customer receipt. |
| `response_code` | `VARCHAR(50)` | `NOT NULL`, `DEFAULT '200_OK'` | Gateway response status. |
| `payment_date` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Transaction execution timestamp. |

### Table 14: `expenses` (Operational Expenditure Ledger)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique expense voucher identifier. |
| `recorded_by_admin_id`| `BIGINT` | `NOT NULL`, `FK` $\rightarrow$ `admins(id)` `ON DELETE RESTRICT` | Admin who logged expense. |
| `expense_category`| `VARCHAR(100)` | `NOT NULL` | `RAW_INGREDIENTS`, `UTILITIES`, `SALARIES`, `EQUIPMENT`, `MARKETING`. |
| `title` | `VARCHAR(150)` | `NOT NULL` | Brief expense title / invoice title. |
| `amount` | `DECIMAL(10,2)` | `NOT NULL`, `CHECK (amount > 0)` | Total expenditure amount. |
| `expense_date` | `DATE` | `NOT NULL` | Incurred expense date. |
| `description` | `TEXT` | `NULL` | Detailed notes / supplier invoice ref. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Record entry timestamp. |

### Table 15: `sales` (Financial Sales & Margin Ledger)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique sale record identifier. |
| `order_id` | `BIGINT` | `NOT NULL`, `UNIQUE`, `FK` $\rightarrow$ `orders(id)` `ON DELETE RESTRICT` | Associated confirmed order. |
| `total_revenue` | `DECIMAL(10,2)` | `NOT NULL` | Gross order value recognized. |
| `total_cogs` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Cost of goods sold (dish prep costs). |
| `net_margin` | `DECIMAL(10,2)` | `NOT NULL` | Gross profit margin (`total_revenue - total_cogs`). |
| `sale_date` | `DATE` | `NOT NULL` | Recognized sale date. |
| `created_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Ledger creation timestamp. |

### Table 16: `reports` (Precomputed Business Intelligence Reports)
| Column Name | Data Type | Constraints | Description |
| :--- | :--- | :--- | :--- |
| `id` | `BIGINT` | `PRIMARY KEY`, `AUTO_INCREMENT` | Unique report record identifier. |
| `generated_by_admin_id`| `BIGINT`| `NOT NULL`, `FK` $\rightarrow$ `admins(id)` `ON DELETE RESTRICT` | Generating administrator. |
| `report_type` | `VARCHAR(50)` | `NOT NULL` | `DAILY_SALES`, `MONTHLY_PROFIT`, `EXPENSE_SUMMARY`, `CATEGORY_ANALYTICS`. |
| `report_title` | `VARCHAR(200)` | `NOT NULL` | Formal title of generated report. |
| `start_date` | `DATE` | `NOT NULL` | Report period start date. |
| `end_date` | `DATE` | `NOT NULL` | Report period end date. |
| `total_revenue` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Total sales revenue in range. |
| `total_expenses` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Total operating expenses in range. |
| `net_profit` | `DECIMAL(10,2)` | `NOT NULL`, `DEFAULT 0.00` | Net Profit (`total_revenue - total_expenses`). |
| `report_data_json` | `LONGTEXT` | `NULL` | Structured JSON summary payload. |
| `generated_at` | `TIMESTAMP` | `NOT NULL`, `DEFAULT CURRENT_TIMESTAMP` | Report generation timestamp. |

---

## 5. Indexing & Query Optimization Strategy

To guarantee sub-500ms query performance (as mandated in `NFR-PERF-01`), the database incorporates strategic B-Tree indexes:

1. `users(email)`: Covered by `UNIQUE` index for instant $O(1)$ login lookups.
2. `foods(category_id, is_available)`: Composite index for high-speed category menu rendering.
3. `orders(customer_id, order_date)`: Composite index for fast customer order history pagination.
4. `orders(order_status)`: Index for real-time kitchen queue dispatcher filtering.
5. `payments(transaction_id)`: Covered by `UNIQUE` index for instant payment reconciliation.
6. `sales(sale_date)` & `expenses(expense_date)`: Single-column date indexes for ultra-fast daily/monthly financial aggregation.
7. `inventory(food_id)`: Covered by `UNIQUE` index for atomic row-level locking during checkout deduction.

---
