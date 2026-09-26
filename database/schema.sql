-- ========================================================================
-- DATABASE SCHEMA: Web-based Food Ordering System
-- Platform: MySQL 8.0+ (InnoDB)
-- Character Set: utf8mb4 / utf8mb4_unicode_ci
-- Course: SE2030 - Software Engineering
-- ========================================================================

CREATE DATABASE IF NOT EXISTS `food_ordering_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `food_ordering_db`;

-- ------------------------------------------------------------------------
-- 1. DROP EXISTING TABLES IN REVERSE DEPENDENCY ORDER
-- ------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `reports`;
DROP TABLE IF EXISTS `sales`;
DROP TABLE IF EXISTS `expenses`;
DROP TABLE IF EXISTS `payments`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `cart_items`;
DROP TABLE IF EXISTS `carts`;
DROP TABLE IF EXISTS `inventory`;
DROP TABLE IF EXISTS `foods`;
DROP TABLE IF EXISTS `food_categories`;
DROP TABLE IF EXISTS `admins`;
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `user_roles`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `users`;

SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------------------
-- 2. AUTHENTICATION & ROLE-BASED ACCESS CONTROL TABLES
-- ------------------------------------------------------------------------

-- Table 1: users
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `is_enabled` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Table 2: roles
CREATE TABLE `roles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(255) NULL
) ENGINE=InnoDB;

-- Table 3: user_roles (Many-to-Many Join Table)
CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_roles_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_roles_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table 4: customers (1:1 with users)
CREATE TABLE `customers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `first_name` VARCHAR(100) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `default_delivery_address` TEXT NOT NULL,
    `city` VARCHAR(100) NOT NULL,
    `postal_code` VARCHAR(20) NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_customers_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table 5: admins (1:1 with users)
CREATE TABLE `admins` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL UNIQUE,
    `full_name` VARCHAR(150) NOT NULL,
    `designation` VARCHAR(100) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_admins_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------
-- 3. FOOD CATALOG & INVENTORY TABLES
-- ------------------------------------------------------------------------

-- Table 6: food_categories
CREATE TABLE `food_categories` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `description` TEXT NULL,
    `image_url` VARCHAR(255) NULL,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Table 7: foods
CREATE TABLE `foods` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `category_id` BIGINT NOT NULL,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT NOT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `cost_price` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `prep_time_minutes` INT NOT NULL DEFAULT 15,
    `image_url` VARCHAR(255) NULL,
    `is_available` BOOLEAN NOT NULL DEFAULT TRUE,
    `is_promotional` BOOLEAN NOT NULL DEFAULT FALSE,
    `discount_percentage` DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_foods_category` FOREIGN KEY (`category_id`) REFERENCES `food_categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table 8: inventory (1:1 with foods)
CREATE TABLE `inventory` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `food_id` BIGINT NOT NULL UNIQUE,
    `stock_quantity` INT NOT NULL DEFAULT 0,
    `low_stock_threshold` INT NOT NULL DEFAULT 10,
    `last_restocked_at` TIMESTAMP NULL,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_inventory_food` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE,
    CONSTRAINT `chk_inventory_stock` CHECK (`stock_quantity` >= 0)
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------
-- 4. SHOPPING CART TABLES
-- ------------------------------------------------------------------------

-- Table 9: carts
CREATE TABLE `carts` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `customer_id` BIGINT NOT NULL UNIQUE,
    `session_token` VARCHAR(100) NULL,
    `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_carts_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table 10: cart_items
CREATE TABLE `cart_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `cart_id` BIGINT NOT NULL,
    `food_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL,
    `subtotal` DECIMAL(10,2) NOT NULL,
    CONSTRAINT `fk_cart_items_cart` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_cart_items_food` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`) ON DELETE CASCADE,
    CONSTRAINT `chk_cart_items_qty` CHECK (`quantity` > 0)
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------
-- 5. ORDERS & ORDER ITEMS TABLES
-- ------------------------------------------------------------------------

-- Table 11: orders
CREATE TABLE `orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_number` VARCHAR(50) NOT NULL UNIQUE,
    `customer_id` BIGINT NOT NULL,
    `order_status` VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    `subtotal_amount` DECIMAL(10,2) NOT NULL,
    `tax_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `delivery_fee` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `total_amount` DECIMAL(10,2) NOT NULL,
    `delivery_address` TEXT NOT NULL,
    `contact_phone` VARCHAR(20) NOT NULL,
    `delivery_notes` TEXT NULL,
    `cancellation_reason` VARCHAR(255) NULL,
    `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_orders_customer` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table 12: order_items (Immutable Snapshot Records)
CREATE TABLE `order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `food_id` BIGINT NOT NULL,
    `food_name` VARCHAR(150) NOT NULL,
    `unit_price` DECIMAL(10,2) NOT NULL,
    `quantity` INT NOT NULL,
    `subtotal` DECIMAL(10,2) NOT NULL,
    CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_order_items_food` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_order_items_qty` CHECK (`quantity` > 0)
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------
-- 6. PAYMENTS, EXPENSES, SALES & REPORTS TABLES
-- ------------------------------------------------------------------------

-- Table 13: payments
CREATE TABLE `payments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL UNIQUE,
    `transaction_id` VARCHAR(100) NOT NULL UNIQUE,
    `payment_method` VARCHAR(50) NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `payment_status` VARCHAR(30) NOT NULL,
    `card_last_four` VARCHAR(4) NULL,
    `response_code` VARCHAR(50) NOT NULL DEFAULT '200_OK',
    `payment_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_payments_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table 14: expenses
CREATE TABLE `expenses` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `recorded_by_admin_id` BIGINT NOT NULL,
    `expense_category` VARCHAR(100) NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL,
    `expense_date` DATE NOT NULL,
    `description` TEXT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_expenses_admin` FOREIGN KEY (`recorded_by_admin_id`) REFERENCES `admins` (`id`) ON DELETE RESTRICT,
    CONSTRAINT `chk_expenses_amt` CHECK (`amount` > 0)
) ENGINE=InnoDB;

-- Table 15: sales
CREATE TABLE `sales` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL UNIQUE,
    `total_revenue` DECIMAL(10,2) NOT NULL,
    `total_cogs` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `net_margin` DECIMAL(10,2) NOT NULL,
    `sale_date` DATE NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_sales_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table 16: reports
CREATE TABLE `reports` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `generated_by_admin_id` BIGINT NOT NULL,
    `report_type` VARCHAR(50) NOT NULL,
    `report_title` VARCHAR(200) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `total_revenue` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `total_expenses` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `net_profit` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    `report_data_json` LONGTEXT NULL,
    `generated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_reports_admin` FOREIGN KEY (`generated_by_admin_id`) REFERENCES `admins` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------------------
-- 7. PERFORMANCE & QUERY INDEXES
-- ------------------------------------------------------------------------
CREATE INDEX `idx_foods_category` ON `foods` (`category_id`, `is_available`);
CREATE INDEX `idx_orders_customer` ON `orders` (`customer_id`, `order_date`);
CREATE INDEX `idx_orders_status` ON `orders` (`order_status`);
CREATE INDEX `idx_cart_items_cart` ON `cart_items` (`cart_id`);
CREATE INDEX `idx_order_items_order` ON `order_items` (`order_id`);
CREATE INDEX `idx_sales_date` ON `sales` (`sale_date`);
CREATE INDEX `idx_expenses_date` ON `expenses` (`expense_date`);
CREATE INDEX `idx_reports_dates` ON `reports` (`start_date`, `end_date`, `report_type`);
