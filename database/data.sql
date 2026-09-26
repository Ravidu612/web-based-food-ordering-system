-- ========================================================================
-- DATABASE SEED DATA: Web-based Food Ordering System
-- Platform: MySQL 8.0+ (InnoDB)
-- Character Set: utf8mb4 / utf8mb4_unicode_ci
-- Course: SE2030 - Software Engineering
-- Note: Default passwords for all seeded users are 'password123'
-- BCrypt Hash: $2a$10$eAccYoNO32OmArPoetdnQOmcx0K.HgphWwU6jWvXG.xX7lPj5dEFe
-- ========================================================================

USE `food_ordering_db`;

-- ------------------------------------------------------------------------
-- 1. SEED ROLES
-- ------------------------------------------------------------------------
INSERT INTO `roles` (`id`, `name`, `description`) VALUES
(1, 'ROLE_ADMIN', 'Restaurant Administrator with full back-office and reporting access'),
(2, 'ROLE_CUSTOMER', 'Registered Customer with menu browsing, cart, and ordering access');

-- ------------------------------------------------------------------------
-- 2. SEED USERS & ROLES ASSIGNMENT
-- Password for all accounts: password123
-- ------------------------------------------------------------------------
INSERT INTO `users` (`id`, `email`, `password`, `is_enabled`) VALUES
(1, 'admin@foodie.com', '$2a$10$eAccYoNO32OmArPoetdnQOmcx0K.HgphWwU6jWvXG.xX7lPj5dEFe', TRUE),
(2, 'john.doe@example.com', '$2a$10$eAccYoNO32OmArPoetdnQOmcx0K.HgphWwU6jWvXG.xX7lPj5dEFe', TRUE),
(3, 'jane.smith@example.com', '$2a$10$eAccYoNO32OmArPoetdnQOmcx0K.HgphWwU6jWvXG.xX7lPj5dEFe', TRUE);

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1), -- Admin
(2, 2), -- Customer 1
(3, 2); -- Customer 2

-- ------------------------------------------------------------------------
-- 3. SEED ADMINS & CUSTOMERS PROFILES
-- ------------------------------------------------------------------------
INSERT INTO `admins` (`id`, `user_id`, `full_name`, `designation`, `phone_number`) VALUES
(1, 1, 'Alexander Wright', 'General Operations Manager', '+1-555-019-2831');

INSERT INTO `customers` (`id`, `user_id`, `first_name`, `last_name`, `phone_number`, `default_delivery_address`, `city`, `postal_code`) VALUES
(1, 2, 'John', 'Doe', '+1-555-014-4920', '742 Evergreen Terrace, Apt 4B', 'Springfield', '97477'),
(2, 3, 'Jane', 'Smith', '+1-555-018-7731', '10880 Wilshire Blvd, Suite 110', 'Los Angeles', '90024');

-- ------------------------------------------------------------------------
-- 4. SEED FOOD CATEGORIES
-- ------------------------------------------------------------------------
INSERT INTO `food_categories` (`id`, `name`, `description`, `image_url`, `is_active`) VALUES
(1, 'Starters & Appetizers', 'Crispy, savory snacks and finger foods to begin your meal', '/images/categories/starters.jpg', TRUE),
(2, 'Signature Burgers', 'Artisan gourmet burgers made with 100% prime beef and fresh brioche buns', '/images/categories/burgers.jpg', TRUE),
(3, 'Stone-Baked Pizzas', 'Traditional Italian hand-stretched pizzas baked in wood-fired ovens', '/images/categories/pizzas.jpg', TRUE),
(4, 'Artisan Pastas', 'Fresh homemade pasta tossed in rich authentic sauces', '/images/categories/pastas.jpg', TRUE),
(5, 'Desserts & Sweets', 'Decadent sweet treats, cheesecakes, and pastries crafted daily', '/images/categories/desserts.jpg', TRUE),
(6, 'Beverages & Mocktails', 'Refreshing iced teas, fresh fruit juices, and hand-spun milkshakes', '/images/categories/beverages.jpg', TRUE);

-- ------------------------------------------------------------------------
-- 5. SEED FOOD ITEMS
-- ------------------------------------------------------------------------
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
-- Category 1: Starters
(1, 1, 'Crispy Garlic Truffle Fries', 'Hand-cut golden potatoes tossed in white truffle oil, sea salt, and aged parmesan.', 7.99, 2.20, 10, '/images/foods/truffle-fries.jpg', TRUE, FALSE, 0.00),
(2, 1, 'Buffalo Glazed Chicken Wings', '8-piece crispy chicken wings tossed in tangy house buffalo sauce with blue cheese dip.', 11.50, 4.00, 15, '/images/foods/buffalo-wings.jpg', TRUE, TRUE, 10.00),
(3, 1, 'Golden Mozzarella Sticks', '6 melted mozzarella batons breaded with Italian herbs, served with marinara dip.', 8.50, 2.80, 12, '/images/foods/mozzarella-sticks.jpg', TRUE, FALSE, 0.00),

-- Category 2: Burgers
(4, 2, 'The Classic Bacon Cheddar Burger', 'Half-pound Angus beef patty, smoked bacon, sharp aged cheddar, lettuce, tomato, and secret sauce on brioche.', 14.99, 5.50, 18, '/images/foods/bacon-burger.jpg', TRUE, TRUE, 15.00),
(5, 2, 'Smoky BBQ Truffle Burger', 'Angus beef patty topped with caramelized onions, swiss cheese, sautéed mushrooms, and BBQ glaze.', 16.50, 6.20, 20, '/images/foods/bbq-burger.jpg', TRUE, FALSE, 0.00),
(6, 2, 'Spicy Peri-Peri Crispy Chicken Burger', 'Buttermilk fried chicken breast, pickled jalapeños, red cabbage slaw, and spicy peri-peri mayo.', 13.99, 4.80, 15, '/images/foods/chicken-burger.jpg', TRUE, FALSE, 0.00),

-- Category 3: Pizzas
(7, 3, 'Authentic Margherita D.O.P.', 'San Marzano tomato sauce, fresh buffalo mozzarella, fragrant sweet basil, and extra virgin olive oil.', 15.00, 4.50, 20, '/images/foods/margherita-pizza.jpg', TRUE, FALSE, 0.00),
(8, 3, 'Double Pepperoni & Hot Honey', 'Loaded with artisanal spicy pepperoni, mozzarella, and drizzled with habanero chili honey.', 18.50, 6.00, 22, '/images/foods/pepperoni-pizza.jpg', TRUE, TRUE, 10.00),
(9, 3, 'Truffle Wild Mushroom Pizza', 'Creamy garlic base, fontina cheese, sautéed wild porcini mushrooms, and fresh rosemary.', 19.99, 6.80, 22, '/images/foods/mushroom-pizza.jpg', TRUE, FALSE, 0.00),

-- Category 4: Pastas
(10, 4, 'Classic Fettuccine Alfredo', 'Fresh egg fettuccine tossed in rich butter, heavy cream, and freshly grated 24-month Parmigiano-Reggiano.', 14.50, 4.20, 15, '/images/foods/pasta-alfredo.jpg', TRUE, FALSE, 0.00),
(11, 4, 'Slow-Cooked Beef Bolognese', 'Traditional 6-hour simmered beef ragù served over al dente rigatoni with fresh herbs.', 16.99, 5.80, 18, '/images/foods/pasta-bolognese.jpg', TRUE, FALSE, 0.00),

-- Category 5: Desserts
(12, 5, 'Warm Belgian Chocolate Lava Cake', 'Molten chocolate center served warm with a scoop of Madagascar vanilla bean gelato.', 8.99, 2.50, 12, '/images/foods/lava-cake.jpg', TRUE, FALSE, 0.00),
(13, 5, 'New York Style Berry Cheesecake', 'Creamy rich cheesecake on a buttery graham crust, topped with fresh strawberry compote.', 7.99, 2.40, 5, '/images/foods/cheesecake.jpg', TRUE, FALSE, 0.00),

-- Category 6: Beverages
(14, 6, 'Fresh Mint Lemonade', 'Freshly squeezed California lemons, crushed garden mint leaves, and pure cane sugar over ice.', 4.50, 0.80, 5, '/images/foods/mint-lemonade.jpg', TRUE, FALSE, 0.00),
(15, 6, 'Salted Caramel Hazelnut Shake', 'Premium vanilla ice cream blended with salted butter caramel and toasted hazelnuts.', 6.50, 1.80, 8, '/images/foods/caramel-shake.jpg', TRUE, FALSE, 0.00);

-- ------------------------------------------------------------------------
-- 6. SEED INVENTORY (1:1 with Foods)
-- ------------------------------------------------------------------------
INSERT INTO `inventory` (`id`, `food_id`, `stock_quantity`, `low_stock_threshold`, `last_restocked_at`) VALUES
(1, 1, 85, 15, NOW()),
(2, 2, 45, 10, NOW()),
(3, 3, 50, 10, NOW()),
(4, 4, 30, 10, NOW()),
(5, 5, 25, 10, NOW()),
(6, 6, 40, 10, NOW()),
(7, 7, 35, 10, NOW()),
(8, 8, 28, 10, NOW()),
(9, 9, 8, 10, NOW()), -- Highlighted as Low Stock (< 10)
(10, 10, 32, 10, NOW()),
(11, 11, 20, 10, NOW()),
(12, 12, 18, 10, NOW()),
(13, 13, 15, 10, NOW()),
(14, 14, 90, 20, NOW()),
(15, 15, 40, 10, NOW());

-- ------------------------------------------------------------------------
-- 7. SEED CARTS & CART ITEMS
-- ------------------------------------------------------------------------
INSERT INTO `carts` (`id`, `customer_id`, `session_token`, `total_amount`) VALUES
(1, 1, 'sess_cust_1_998124', 22.98),
(2, 2, 'sess_cust_2_881239', 0.00);

INSERT INTO `cart_items` (`id`, `cart_id`, `food_id`, `quantity`, `unit_price`, `subtotal`) VALUES
(1, 1, 4, 1, 14.99, 14.99), -- 1x Bacon Cheddar Burger
(2, 1, 1, 1, 7.99, 7.99);   -- 1x Truffle Fries

-- ------------------------------------------------------------------------
-- 8. SEED ORDERS & ORDER ITEMS
-- ------------------------------------------------------------------------
-- Order 1: Delivered Order
INSERT INTO `orders` (`id`, `order_number`, `customer_id`, `order_status`, `subtotal_amount`, `tax_amount`, `delivery_fee`, `total_amount`, `delivery_address`, `contact_phone`, `delivery_notes`, `order_date`) VALUES
(1, 'ORD-20260901-1001', 1, 'DELIVERED', 33.49, 1.67, 3.50, 38.66, '742 Evergreen Terrace, Apt 4B, Springfield', '+1-555-014-4920', 'Please ring the doorbell twice.', '2026-09-01 13:25:00'),
(2, 'ORD-20260902-1002', 2, 'PREPARING', 35.49, 1.77, 3.50, 40.76, '10880 Wilshire Blvd, Suite 110, Los Angeles', '+1-555-018-7731', 'Leave package at front security desk.', '2026-09-02 18:40:00'),
(3, 'ORD-20260903-1003', 1, 'CONFIRMED', 25.00, 1.25, 3.50, 29.75, '742 Evergreen Terrace, Apt 4B, Springfield', '+1-555-014-4920', 'Extra napkins please.', NOW());

INSERT INTO `order_items` (`id`, `order_id`, `food_id`, `food_name`, `unit_price`, `quantity`, `subtotal`) VALUES
-- Items for Order 1
(1, 1, 4, 'The Classic Bacon Cheddar Burger', 14.99, 1, 14.99),
(2, 1, 2, 'Buffalo Glazed Chicken Wings', 11.50, 1, 11.50),
(3, 1, 12, 'Warm Belgian Chocolate Lava Cake', 7.00, 1, 7.00),

-- Items for Order 2
(4, 2, 8, 'Double Pepperoni & Hot Honey', 18.50, 1, 18.50),
(5, 2, 11, 'Slow-Cooked Beef Bolognese', 16.99, 1, 16.99),

-- Items for Order 3
(6, 3, 7, 'Authentic Margherita D.O.P.', 15.00, 1, 15.00),
(7, 3, 1, 'Crispy Garlic Truffle Fries', 7.99, 1, 7.99),
(8, 3, 14, 'Fresh Mint Lemonade', 2.01, 1, 2.01);

-- ------------------------------------------------------------------------
-- 9. SEED PAYMENTS
-- ------------------------------------------------------------------------
INSERT INTO `payments` (`id`, `order_id`, `transaction_id`, `payment_method`, `amount`, `payment_status`, `card_last_four`, `response_code`, `payment_date`) VALUES
(1, 1, 'TXN-9948102941', 'CARD', 38.66, 'SUCCESS', '4242', '200_OK', '2026-09-01 13:26:15'),
(2, 2, 'TXN-9948102942', 'CARD', 40.76, 'SUCCESS', '8812', '200_OK', '2026-09-02 18:41:10'),
(3, 3, 'TXN-9948102943', 'CARD', 29.75, 'SUCCESS', '4242', '200_OK', NOW());

-- ------------------------------------------------------------------------
-- 10. SEED OPERATIONAL EXPENSES
-- ------------------------------------------------------------------------
INSERT INTO `expenses` (`id`, `recorded_by_admin_id`, `expense_category`, `title`, `amount`, `expense_date`, `description`) VALUES
(1, 1, 'RAW_INGREDIENTS', 'Weekly Fresh Angus Beef & Poultry Procurement', 450.00, '2026-09-01', 'Invoice #INV-9921 from Metro Wholesale Farms'),
(2, 1, 'RAW_INGREDIENTS', 'Artisanal Cheeses & Italian Flour Import', 220.50, '2026-09-01', 'Invoice #INV-3312 from Parma Direct Goods'),
(3, 1, 'UTILITIES', 'Commercial Kitchen Electricity & Gas Utility Bill', 315.00, '2026-09-02', 'Monthly municipal energy utility statement'),
(4, 1, 'MARKETING', 'Digital Social Media Promotion Campaign', 120.00, '2026-09-03', 'Google & Instagram local delivery ads');

-- ------------------------------------------------------------------------
-- 11. SEED SALES & REVENUE RECORDS
-- ------------------------------------------------------------------------
INSERT INTO `sales` (`id`, `order_id`, `total_revenue`, `total_cogs`, `net_margin`, `sale_date`) VALUES
(1, 1, 38.66, 12.00, 26.66, '2026-09-01'),
(2, 2, 40.76, 11.80, 28.96, '2026-09-02'),
(3, 3, 29.75, 7.50, 22.25, '2026-09-03');

-- ------------------------------------------------------------------------
-- 12. SEED AGGREGATED REPORTS
-- ------------------------------------------------------------------------
INSERT INTO `reports` (`id`, `generated_by_admin_id`, `report_type`, `report_title`, `start_date`, `end_date`, `total_revenue`, `total_expenses`, `net_profit`, `report_data_json`, `generated_at`) VALUES
(1, 1, 'MONTHLY_PROFIT', 'Executive September Operational Profitability Summary', '2026-09-01', '2026-09-30', 109.17, 1105.50, -996.33, '{"totalOrders": 3, "averageOrderValue": 36.39, "topCategory": "Signature Burgers", "inventoryHealth": "Optimal"}', NOW());
