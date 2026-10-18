-- ========================================================================
-- Add 10 Unique Food Items for Each Category (Total 60 items)
-- All prices in LKR (Range: LKR 650 - 2500)
-- ========================================================================

USE `food_ordering_db`;

-- Category 1: Starters & Appetizers (id: 1) - 7 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(16, 1, 'Loaded Cheesy Jalapeno Nachos', 'Crispy tortilla chips layered with melted cheddar, pickled jalapeños, pico de gallo, and sour cream.', 1450.00, 450.00, 12, 'https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(17, 1, 'Crispy Calamari Rings', 'Tender spiced calamari tossed in sea salt and cracked pepper, served with tangy garlic aioli.', 1750.00, 600.00, 15, '/images/foods/calamari.jpg', TRUE, FALSE, 0.00),
(18, 1, 'Creamy Spinach & Artichoke Dip', 'Warm creamy blend of baby spinach, tender artichoke hearts, parmesan, served with warm pita chips.', 1350.00, 420.00, 12, 'https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(19, 1, 'Honey BBQ Chicken Bites', 'Crispy battered tender chicken breast bites tossed in sweet smoky barbecue glaze.', 1500.00, 480.00, 15, 'https://images.unsplash.com/photo-1562967914-608f82629710?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 10.00),
(20, 1, 'Bruschetta al Pomodoro', 'Toasted artisan baguette slices topped with ripe heirloom tomatoes, fresh basil, garlic, and balsamic glaze.', 1050.00, 320.00, 10, 'https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(21, 1, 'Prawn Tempura with Sweet Chili', 'Golden crispy jumbo prawns in light Japanese tempura batter with sweet chili lime dip.', 1850.00, 680.00, 14, 'https://images.unsplash.com/photo-1625944525533-473f1a3d54e7?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(22, 1, 'Creamy Garlic Wild Mushroom Crostini', 'Sautéed wild forest mushrooms in white wine garlic herb butter over toasted sourdough.', 1250.00, 390.00, 12, 'https://images.unsplash.com/photo-1541529086526-db283c563270?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Category 2: Signature Burgers (id: 2) - 7 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(23, 2, 'Double Smash Cheeseburger', 'Two seared Angus beef patties, double American cheese, caramelized onions, pickles, and diner smash sauce.', 2450.00, 850.00, 15, 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(24, 2, 'Swiss Mushroom Melt Burger', 'Flame-broiled beef patty smothered in melted Swiss cheese and sautéed garlic butter mushrooms.', 2250.00, 780.00, 18, 'https://images.unsplash.com/photo-1550547660-d9450f859349?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(25, 2, 'Crispy Nashville Hot Chicken Burger', 'Southern fried spicy chicken breast, cayenne chili glaze, crunchy coleslaw, and dill pickles.', 2050.00, 690.00, 16, 'https://images.unsplash.com/photo-1606755962773-d324e0a13086?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 12.00),
(26, 2, 'Truffle Black Angus Steakhouse Burger', 'Premium minced steak patty, melted gouda, truffle mayo, wild arugula, and tomato jam.', 2500.00, 920.00, 20, 'https://images.unsplash.com/photo-1594212699903-ec8a3eca50f5?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(27, 2, 'Hawaiian Teriyaki Chicken Burger', 'Grilled chicken breast, sweet teriyaki glaze, grilled pineapple ring, and crisp lettuce on brioche.', 1950.00, 640.00, 16, 'https://images.unsplash.com/photo-1520072959219-c595dc870360?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(28, 2, 'Chipotle Black Bean Veggie Burger', 'Artisan spiced black bean & corn patty, avocado guacamole, pepper jack cheese, and chipotle crema.', 1750.00, 550.00, 15, 'https://images.unsplash.com/photo-1525059696034-4967a8e1dca2?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(29, 2, 'Spicy Blue Cheese Crunch Burger', 'Angus patty, crumbled Danish blue cheese, crispy onion rings, hot buffalo drizzle, and lettuce.', 2300.00, 810.00, 18, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Category 3: Stone-Baked Pizzas (id: 3) - 7 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(30, 3, 'Quattro Formaggi (Four Cheese)', 'Mozzarella, gorgonzola, aged parmesan, and creamy ricotta on a white garlic olive oil base.', 2350.00, 780.00, 20, 'https://images.unsplash.com/photo-1573821663912-569905455b1c?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(31, 3, 'BBQ Smoked Chicken & Bacon Pizza', 'Smoky barbecue sauce base, roasted chicken strips, smoked bacon, red onions, and fresh cilantro.', 2450.00, 820.00, 22, 'https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(32, 3, 'Spicy Diavola & Jalapeno Pizza', 'Fiery Italian salami, sliced red chillies, pickled jalapeños, mozzarella, and crushed chili flakes.', 2400.00, 790.00, 20, 'https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 10.00),
(33, 3, 'Mediterranean Roasted Veggie Pizza', 'Basil pesto base, grilled zucchini, bell peppers, kalamata olives, sundried tomatoes, and feta cheese.', 2100.00, 680.00, 18, 'https://images.unsplash.com/photo-1590947132387-155cc02f3212?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(34, 3, 'Prosciutto e Rucola Pizza', 'Crispy crust with fresh mozzarella, topped with cured prosciutto crudo, baby arugula, and shaved parmesan.', 2500.00, 920.00, 22, 'https://images.unsplash.com/photo-1541745537411-b8046dc6d66c?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(35, 3, 'Garlic Butter Seafood Supreme Pizza', 'Calamari, succulent shrimp, garlic butter, fresh parsley, mozzarella, and lemon zest.', 2500.00, 950.00, 24, 'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(36, 3, 'Tandoori Spiced Chicken Pizza', 'Marinated tandoori chicken chunks, spiced onion rings, green peppers, mozzarella, and mint swirl.', 2300.00, 760.00, 20, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Category 4: Artisan Pastas (id: 4) - 8 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(37, 4, 'Creamy Truffle Carbonara', 'Spaghetti tossed with crisp smoked pancetta, free-range egg yolks, pecorino romano, and black pepper.', 2250.00, 740.00, 18, 'https://images.unsplash.com/photo-1612874742237-6526221588e3?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(38, 4, 'Seafood Linguine Marinara', 'Tender shrimp, calamari, and mussels tossed in a rich garlic white wine tomato marinara sauce.', 2450.00, 890.00, 20, 'https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(39, 4, 'Creamy Pesto Genovese Penne', 'Fresh basil pesto, toasted pine nuts, roasted cherry tomatoes, and shaved parmesan over al dente penne.', 1850.00, 560.00, 15, 'https://images.unsplash.com/photo-1551183053-bf91a1d81141?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(40, 4, 'Spicy Penne all Arrabbiata', 'Penne pasta tossed in a fiery San Marzano tomato sauce infused with garlic, red chilies, and fresh parsley.', 1650.00, 480.00, 14, 'https://images.unsplash.com/photo-1621996346565-e3d5d6281781?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 15.00),
(41, 4, 'Four-Cheese Stuffed Ravioli', 'Handmade egg ravioli filled with ricotta, fontina, gorgonzola, in a sage and brown butter sauce.', 2200.00, 710.00, 18, '/images/foods/ravioli.jpg', TRUE, FALSE, 0.00),
(42, 4, 'Grilled Chicken & Mushroom Rigatoni', 'Tender sliced chicken breast and wild portobello mushrooms in a garlic thyme cream sauce.', 2100.00, 680.00, 18, 'https://images.unsplash.com/photo-1608897013039-887f21d8c804?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(43, 4, 'Traditional Baked Beef Lasagna', 'Layers of fresh pasta sheets, slow-braised beef ragù, velvety bechamel, and melted mozzarella.', 2300.00, 770.00, 22, 'https://images.unsplash.com/photo-1574894709920-11b28e7367e3?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(44, 4, 'Prawn & Garlic Aglio e Olio', 'Spaghetti tossed in extra virgin olive oil, sautéed garlic slices, chili flakes, and pan-seared tiger prawns.', 2350.00, 820.00, 16, '/images/foods/prawn-pasta.jpg', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Category 5: Desserts & Sweets (id: 5) - 8 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(45, 5, 'Classic Italian Tiramisu', 'Espresso-soaked ladyfingers layered with velvety mascarpone cream and dusted with dark cocoa powder.', 1250.00, 360.00, 8, 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(46, 5, 'Salted Caramel Pecan Tart', 'Buttery crisp pastry shell filled with gooey golden caramel and toasted southern pecans with vanilla cream.', 1200.00, 350.00, 8, 'https://images.unsplash.com/photo-1519869325930-281384150729?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(47, 5, 'Warm Apple Cinnamon Crumble', 'Spiced Granny Smith apples baked under a crunchy oat streusel crust, served with warm vanilla custard.', 1100.00, 320.00, 12, 'https://images.unsplash.com/photo-1568571780765-9276ac8b75a2?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(48, 5, 'Triple Chocolate Fudge Brownie Sundae', 'Fudgy dark chocolate walnut brownie topped with vanilla bean gelato, hot chocolate fudge, and whipped cream.', 1350.00, 390.00, 10, 'https://images.unsplash.com/photo-1564355808539-22fda35bed7e?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 10.00),
(49, 5, 'Red Velvet Cream Cheese Cupcakes', 'Platter of moist red velvet cupcakes topped with whipped Madagascar vanilla cream cheese frosting.', 1150.00, 330.00, 6, 'https://images.unsplash.com/photo-1614707267537-b85aaf00c4b7?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(50, 5, 'Mango Passionfruit Panna Cotta', 'Silky smooth Italian cream panna cotta crowned with tropical Alphonso mango and passionfruit reduction.', 1100.00, 310.00, 6, 'https://images.unsplash.com/photo-1488477181946-6428a0291777?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(51, 5, 'Crispy Churros con Chocolate', 'Crispy golden fried pastries coated in cinnamon sugar, served with rich warm Spanish dipping chocolate.', 1050.00, 290.00, 10, 'https://images.unsplash.com/photo-1624371414361-e670edf4898d?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(52, 5, 'Nutella & Banana French Crepe', 'Thin French crepe filled with melted warm Nutella, fresh sliced banana, and toasted hazelnut crunch.', 1250.00, 360.00, 10, 'https://images.unsplash.com/photo-1519676867240-f03562e64548?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Category 6: Beverages & Mocktails (id: 6) - 8 New items (Total 10)
INSERT INTO `foods` (`id`, `category_id`, `name`, `description`, `price`, `cost_price`, `prep_time_minutes`, `image_url`, `is_available`, `is_promotional`, `discount_percentage`) VALUES
(53, 6, 'Passionfruit Mango Sparkling Cooler', 'Fresh passionfruit pulp, tropical mango juice, crushed ice, and effervescent sparkling soda.', 850.00, 210.00, 5, 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(54, 6, 'Iced Berry Hibiscus Tea', 'Brewed organic wild hibiscus tea infused with crushed fresh strawberries, mint, and a touch of honey.', 750.00, 180.00, 5, 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(55, 6, 'Classic Virgin Mojito', 'Freshly muddled Persian lime wedges, garden mint, cane sugar syrup, and fizzy club soda over crushed ice.', 800.00, 190.00, 5, 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(56, 6, 'Oreo Cookie Overload Thickshake', 'Rich vanilla ice cream blended with crushed Oreo cookies, chocolate drizzle, and whipped cream.', 980.00, 290.00, 6, 'https://images.unsplash.com/photo-1572490122747-3968b75cc699?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(57, 6, 'Cold Brew Iced Vanilla Latte', '16-hour steeped smooth cold brew coffee combined with creamy milk and Madagascar vanilla syrup.', 850.00, 220.00, 4, 'https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00),
(58, 6, 'Tropical Blue Ocean Mocktail', 'Blue curacao syrup, pineapple juice, coconut water, and a splash of sprite garnished with maraschino cherries.', 900.00, 240.00, 5, 'https://images.unsplash.com/photo-1536935338788-846bb9981813?auto=format&fit=crop&w=600&q=80', TRUE, TRUE, 10.00),
(59, 6, 'Watermelon Basil Sparkler', 'Freshly crushed sweet red watermelon juice with bruised sweet basil leaves and sparkling lime water.', 750.00, 170.00, 5, '/images/foods/watermelon-drink.jpg', TRUE, FALSE, 0.00),
(60, 6, 'Spiced Ceylon Masala Chai Latte', 'Bold black Ceylon tea slow-brewed with crushed cardamom, cinnamon, cloves, ginger, and steamed milk.', 680.00, 150.00, 6, 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=600&q=80', TRUE, FALSE, 0.00)
ON DUPLICATE KEY UPDATE name=VALUES(name), description=VALUES(description), price=VALUES(price), cost_price=VALUES(cost_price), image_url=VALUES(image_url);

-- Seed Inventory for items 16 to 60 (healthy stock 25 to 55 units)
INSERT INTO `inventory` (`id`, `food_id`, `stock_quantity`, `low_stock_threshold`, `last_restocked_at`) VALUES
(16, 16, 45, 10, NOW()),
(17, 17, 30, 10, NOW()),
(18, 18, 35, 10, NOW()),
(19, 19, 50, 10, NOW()),
(20, 20, 40, 10, NOW()),
(21, 21, 28, 10, NOW()),
(22, 22, 35, 10, NOW()),
(23, 23, 40, 10, NOW()),
(24, 24, 32, 10, NOW()),
(25, 25, 38, 10, NOW()),
(26, 26, 25, 10, NOW()),
(27, 27, 36, 10, NOW()),
(28, 28, 42, 10, NOW()),
(29, 29, 30, 10, NOW()),
(30, 30, 28, 10, NOW()),
(31, 31, 34, 10, NOW()),
(32, 32, 29, 10, NOW()),
(33, 33, 32, 10, NOW()),
(34, 34, 24, 10, NOW()),
(35, 35, 22, 10, NOW()),
(36, 36, 35, 10, NOW()),
(37, 37, 30, 10, NOW()),
(38, 38, 25, 10, NOW()),
(39, 39, 40, 10, NOW()),
(40, 40, 45, 10, NOW()),
(41, 41, 26, 10, NOW()),
(42, 42, 33, 10, NOW()),
(43, 43, 28, 10, NOW()),
(44, 44, 31, 10, NOW()),
(45, 45, 35, 10, NOW()),
(46, 46, 28, 10, NOW()),
(47, 47, 30, 10, NOW()),
(48, 48, 32, 10, NOW()),
(49, 49, 40, 10, NOW()),
(50, 50, 35, 10, NOW()),
(51, 51, 45, 10, NOW()),
(52, 52, 38, 10, NOW()),
(53, 53, 50, 15, NOW()),
(54, 54, 55, 15, NOW()),
(55, 55, 60, 15, NOW()),
(56, 56, 45, 10, NOW()),
(57, 57, 48, 10, NOW()),
(58, 58, 42, 10, NOW()),
(59, 59, 52, 15, NOW()),
(60, 60, 65, 20, NOW())
ON DUPLICATE KEY UPDATE stock_quantity=VALUES(stock_quantity), low_stock_threshold=VALUES(low_stock_threshold);
