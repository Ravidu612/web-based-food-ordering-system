package com.foodie.app.config;

import com.foodie.app.entity.*;
import com.foodie.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final FoodCategoryRepository categoryRepository;
    private final FoodRepository foodRepository;
    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Checking database seed status...");

        // Ensure Admin Role exists
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> {
            log.info("Creating ROLE_ADMIN...");
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setDescription("Administrator with full access");
            return roleRepository.save(role);
        });

        // Ensure Customer Role exists
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER").orElseGet(() -> {
            log.info("Creating ROLE_CUSTOMER...");
            Role role = new Role();
            role.setName("ROLE_CUSTOMER");
            role.setDescription("Registered Customer");
            return roleRepository.save(role);
        });

        // Ensure Admin User exists and reset password if needed
        User adminUser = userRepository.findByEmail("admin@foodie.com").orElse(null);
        if (adminUser == null) {
            log.info("Seeding default admin user...");
            adminUser = User.builder()
                    .email("admin@foodie.com")
                    .password(passwordEncoder.encode("password123"))
                    .isEnabled(true)
                    .roles(new HashSet<>(Collections.singletonList(adminRole)))
                    .build();

            adminUser = userRepository.save(adminUser);

            Admin adminProfile = Admin.builder()
                    .user(adminUser)
                    .fullName("Alexander Wright")
                    .designation("General Operations Manager")
                    .phoneNumber("+1-555-019-2831")
                    .build();

            adminRepository.save(adminProfile);
            log.info("Default admin user created: admin@foodie.com / password123");
        } else {
            log.info("Admin user already exists. Resetting password to password123 for testing...");
            adminUser.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(adminUser);
        }

        // Ensure default Customer User exists
        User customerUser = userRepository.findByEmail("john.doe@example.com").orElse(null);
        if (customerUser == null) {
            log.info("Seeding default customer user...");
            customerUser = User.builder()
                    .email("john.doe@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .isEnabled(true)
                    .roles(new HashSet<>(Collections.singletonList(customerRole)))
                    .build();
            customerUser = userRepository.save(customerUser);

            Customer customerProfile = Customer.builder()
                    .user(customerUser)
                    .firstName("John")
                    .lastName("Doe")
                    .phoneNumber("+1-555-014-4920")
                    .defaultDeliveryAddress("742 Evergreen Terrace, Apt 4B")
                    .city("Springfield")
                    .postalCode("97477")
                    .build();
            customerProfile = customerRepository.save(customerProfile);

            Cart cart = Cart.builder()
                    .customer(customerProfile)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            cartRepository.save(cart);

            log.info("Default customer user created: john.doe@example.com / password123");
        } else {
            log.info("Resetting password for john.doe@example.com to password123");
            customerUser.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(customerUser);
        }

        // Seed Food Categories, Foods, and Inventory if empty
        if (categoryRepository.count() == 0) {
            log.info("Seeding initial Food Categories & Items...");

            FoodCategory starters = createCategory("Starters & Appetizers", "Crispy, savory snacks and finger foods to begin your meal", "/images/categories/starters.jpg");
            FoodCategory burgers = createCategory("Signature Burgers", "Artisan gourmet burgers made with 100% prime beef and fresh brioche buns", "/images/categories/burgers.jpg");
            FoodCategory pizzas = createCategory("Stone-Baked Pizzas", "Traditional Italian hand-stretched pizzas baked in wood-fired ovens", "/images/categories/pizzas.jpg");
            FoodCategory pastas = createCategory("Artisan Pastas", "Fresh homemade pasta tossed in rich authentic sauces", "/images/categories/pastas.jpg");
            FoodCategory desserts = createCategory("Desserts & Sweets", "Decadent sweet treats, cheesecakes, and pastries crafted daily", "/images/categories/desserts.jpg");
            FoodCategory beverages = createCategory("Beverages & Mocktails", "Refreshing iced teas, fresh fruit juices, and hand-spun milkshakes", "/images/categories/beverages.jpg");

            createFood(starters, "Crispy Garlic Truffle Fries", "Hand-cut golden potatoes tossed in white truffle oil, sea salt, and aged parmesan.", new BigDecimal("7.99"), new BigDecimal("2.20"), 10, "/images/foods/truffle-fries.jpg", true, false, BigDecimal.ZERO, 85);
            createFood(starters, "Buffalo Glazed Chicken Wings", "8-piece crispy chicken wings tossed in tangy house buffalo sauce with blue cheese dip.", new BigDecimal("11.50"), new BigDecimal("4.00"), 15, "/images/foods/buffalo-wings.jpg", true, true, new BigDecimal("10.00"), 45);
            createFood(burgers, "The Classic Bacon Cheddar Burger", "Half-pound Angus beef patty, smoked bacon, sharp aged cheddar, lettuce, tomato, and secret sauce on brioche.", new BigDecimal("14.99"), new BigDecimal("5.50"), 18, "/images/foods/bacon-burger.jpg", true, true, new BigDecimal("15.00"), 30);
            createFood(burgers, "Smoky BBQ Truffle Burger", "Angus beef patty topped with caramelized onions, swiss cheese, sautéed mushrooms, and BBQ glaze.", new BigDecimal("16.50"), new BigDecimal("6.20"), 20, "/images/foods/bbq-burger.jpg", true, false, BigDecimal.ZERO, 25);
            createFood(pizzas, "Authentic Margherita D.O.P.", "San Marzano tomato sauce, fresh buffalo mozzarella, fragrant sweet basil, and extra virgin olive oil.", new BigDecimal("15.00"), new BigDecimal("4.50"), 20, "/images/foods/margherita-pizza.jpg", true, false, BigDecimal.ZERO, 35);
            createFood(pizzas, "Double Pepperoni & Hot Honey", "Loaded with artisanal spicy pepperoni, mozzarella, and drizzled with habanero chili honey.", new BigDecimal("18.50"), new BigDecimal("6.00"), 22, "/images/foods/pepperoni-pizza.jpg", true, true, new BigDecimal("10.00"), 28);
            createFood(pastas, "Classic Fettuccine Alfredo", "Fresh egg fettuccine tossed in rich butter, heavy cream, and freshly grated 24-month Parmigiano-Reggiano.", new BigDecimal("14.50"), new BigDecimal("4.20"), 15, "/images/foods/pasta-alfredo.jpg", true, false, BigDecimal.ZERO, 32);
            createFood(desserts, "Warm Belgian Chocolate Lava Cake", "Molten chocolate center served warm with a scoop of Madagascar vanilla bean gelato.", new BigDecimal("8.99"), new BigDecimal("2.50"), 12, "/images/foods/lava-cake.jpg", true, false, BigDecimal.ZERO, 18);
            createFood(beverages, "Fresh Mint Lemonade", "Freshly squeezed California lemons, crushed garden mint leaves, and pure cane sugar over ice.", new BigDecimal("4.50"), new BigDecimal("0.80"), 5, "/images/foods/mint-lemonade.jpg", true, false, BigDecimal.ZERO, 90);

            log.info("Catalog seeding completed successfully!");
        }
    }

    private FoodCategory createCategory(String name, String description, String imageUrl) {
        FoodCategory cat = FoodCategory.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .isActive(true)
                .build();
        return categoryRepository.save(cat);
    }

    private void createFood(FoodCategory category, String name, String description, BigDecimal price, BigDecimal costPrice,
                            Integer prepTime, String imageUrl, boolean available, boolean promo, BigDecimal discountPct, int initialStock) {
        Food food = Food.builder()
                .category(category)
                .name(name)
                .description(description)
                .price(price)
                .costPrice(costPrice)
                .prepTimeMinutes(prepTime)
                .imageUrl(imageUrl)
                .isAvailable(available)
                .isPromotional(promo)
                .discountPercentage(discountPct)
                .build();
        food = foodRepository.save(food);

        Inventory inv = Inventory.builder()
                .food(food)
                .stockQuantity(initialStock)
                .lowStockThreshold(10)
                .lastRestockedAt(LocalDateTime.now())
                .build();
        inventoryRepository.save(inv);
    }
}

