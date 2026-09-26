# Phase 6: Frontend Implementation Design

**Project:** Web-Based Food Ordering System — FoodieExpress  
**Technology:** Thymeleaf 3 + Bootstrap 5.3 + Vanilla JavaScript + CSS3  
**Document Version:** 1.0  
**Author:** Software Engineering Group Project Team

---

## 1. Design Philosophy

The frontend follows a **mobile-first, component-driven** design strategy using Thymeleaf
server-side rendering (SSR). The customer portal prioritises discovery, cart management,
and order transparency; the admin back-office prioritises operational control and data
clarity.

### 1.1 Core Design Principles

| Principle | Implementation |
|-----------|----------------|
| Consistency | Shared CSS design tokens (`--primary`, `--dark`, `--gradient-*`) used everywhere |
| Hierarchy | Bootstrap 5 grid + utility classes; custom `.kpi-card`, `.food-card`, `.admin-sidebar` |
| Accessibility | Semantic HTML5, `aria-*` attributes on interactive elements, `alt` on all images |
| Security | Thymeleaf CSRF tokens on all `POST` forms; Spring Security `sec:authorize` on nav items |
| Performance | Bootstrap CDN for cache hits; scoped CSS; no unnecessary JavaScript frameworks |

---

## 2. Template Inventory

### 2.1 Shared Layout Fragments (`templates/layout/`)

| File | Fragment Names | Purpose |
|------|---------------|---------|
| `main.html` | `head(title)`, `navbar`, `alerts`, `footer` | Customer-facing base layout |
| `admin-layout.html` | `head(title)`, `admin-navbar`, `admin-sidebar(activeTab)` | Admin back-office base layout |

The `admin-sidebar` fragment accepts a **`activeTab`** parameter (e.g. `'dashboard'`,
`'orders'`, `'inventory'`) that dynamically applies `.active` class via Thymeleaf
`th:classappend`.

### 2.2 Authentication Views (`templates/auth/`)

| Template | Controller Mapping | Model Attributes |
|----------|-------------------|-----------------|
| `login.html` | `GET /login` | `param.error`, `param.logout` |
| `register.html` | `GET /register` | `registrationDto` (RegistrationDto) |

### 2.3 Customer Portal Views (`templates/customer/`)

| Template | Controller | Key Model Attributes |
|----------|-----------|---------------------|
| `index.html` | `GET /` | `categories`, `promotionalFoods`, `featuredFoods` |
| `menu.html` | `GET /menu` | `foods`, `categories`, `keyword`, `selectedCategoryId`, `promotionalOnly` |
| `food-details.html` | `GET /foods/{id}` | `food` (FoodDto) |
| `cart.html` | `GET /cart` | `cart` (CartDto) |
| `checkout.html` | `GET /checkout` | `cart` (CartDto), `checkoutDto` (CheckoutDto) |
| `order-confirmation.html` | `GET /orders/{id}/confirmation` | `order` (OrderDto) |
| `orders.html` | `GET /orders` | `orders` (List of OrderSummaryDto) |
| `order-details.html` | `GET /orders/{id}` | `order` (OrderDto with `canBeCancelled`) |
| `about.html` | `GET /about` | — |
| `contact.html` | `GET /contact` | — |

### 2.4 Admin Back-Office Views (`templates/admin/`)

| Template | Controller | Key Model Attributes |
|----------|-----------|---------------------|
| `dashboard.html` | `GET /admin/dashboard` | `summary` (DashboardSummaryDto), `recentOrders` |
| `categories.html` | `GET /admin/categories` | `categories` (List of CategoryDto), `categoryDto` |
| `foods.html` | `GET /admin/foods` | `foods` (List of FoodDto), `foodDto`, `categories` |
| `inventory.html` | `GET /admin/inventory` | `inventoryList` (List of Inventory), `lowStockCount` |
| `orders.html` | `GET /admin/orders` | `orders` (List of OrderDto), `allStatuses` |
| `customers.html` | `GET /admin/customers` | `customers` (List of CustomerDto), `totalCustomers` |
| `expenses.html` | `GET /admin/reports/expenses` | `expenses`, `expenseSummary`, `expenseDto` |
| `sales-report.html` | `GET /admin/reports/sales` | `report`, `dailySales`, `topFoods`, `startDate`, `endDate` |
| `profit-report.html` | `GET /admin/reports/profit` | `pl` (ProfitLossDto), `period`, `periodLabel` |

### 2.5 Error Views (`templates/error/`)

| Template | HTTP Status | Description |
|----------|------------|-------------|
| `403.html` | 403 Forbidden | RBAC access denied |
| `custom-error.html` | 4xx / 5xx | Generic fallback with dynamic status, message |

---

## 3. Static Assets

### 3.1 CSS Design System (`static/css/style.css`)

Key custom components defined:
- `:root` variables — `--primary`, `--primary-dark`, `--dark`, `--gradient-hero`
- `.btn-primary-custom` — Gradient CTA with hover lift animation
- `.hero-section` — Dark radial gradient hero banner
- `.hero-highlight` — Animated shimmer gradient text effect
- `.food-card` — Dish card with image zoom + shadow on hover
- `.badge-promo` / `.badge-prep` — Absolute-positioned dish image badges
- `.category-pill` — Active/hover pill for menu filtering
- `.admin-wrapper` — CSS Grid: sidebar + main content
- `.admin-sidebar` — Dark sidebar with section headings
- `.kpi-card` — Executive metric cards with icon and value
- `.status-timeline` — 4-step horizontal order progress tracker

### 3.2 JavaScript (`static/js/app.js`)

| Function | Trigger | Purpose |
|----------|---------|---------|
| Card number formatter | `input` on `#cardNumber` | Restricts to 16 numeric digits |
| Card expiry formatter | `input` on `#cardExpiry` | Auto-inserts `/` at MM/YY |
| CVV formatter | `input` on `#cardCvv` | Restricts to 3 digits |
| Alert auto-dismiss | `DOMContentLoaded` | Closes `.alert-dismissible` after 5 seconds |

---

## 4. Key UI Patterns

### 4.1 Fragment Composition

All customer pages:
```html
<head th:replace="~{layout/main :: head('Page Title')}">
<nav th:replace="~{layout/main :: navbar}">
<div th:replace="~{layout/main :: alerts}">
<footer th:replace="~{layout/main :: footer}">
```

All admin pages:
```html
<head th:replace="~{layout/admin-layout :: head('Page Title')}">
<nav th:replace="~{layout/admin-layout :: admin-navbar}">
<div th:replace="~{layout/admin-layout :: admin-sidebar('activeTabKey')}">
```

### 4.2 Spring Security Dialect

```html
<li sec:authorize="hasRole('ADMIN')">Admin links only visible to admins</li>
<div sec:authorize="isAnonymous()">Login/Register only for guests</div>
<span sec:authentication="name">Displays username in navbar</span>
```

### 4.3 CSRF Protection

Spring Security's `CsrfRequestDataValueProcessor` automatically injects CSRF tokens into all Thymeleaf `th:action` POST forms — no manual hidden input needed.

### 4.4 Admin Order Status Advancement

```
CONFIRMED → PREPARING → OUT_FOR_DELIVERY → DELIVERED
```
Per-row `<select>` form POSTs to `/admin/orders/update-status` with `orderId` + `newStatus`.

### 4.5 Inline CRUD Modals

- **Add**: Single global modal binds fresh DTO (`#addCategoryModal`, `#addFoodModal`)
- **Edit**: Per-row modal `#editFoodModal{id}` pre-populated with `th:value`
- **Restock**: Per-row `#restockModal{foodId}` showing current stock

### 4.6 Order Status Timeline

4-step horizontal stepper with `th:classappend` adding `.completed` class based on `order.orderStatus`. Cancelled orders show `.alert-danger` panel instead of the stepper.

---

## 5. Template–Controller–DTO Consistency

| Controller Action | Template | DTO Key | Verified |
|-------------------|----------|---------|----------|
| `HomeController.home()` | `customer/index.html` | `featuredFoods`, `categories` | YES |
| `FoodController.menuCatalog()` | `customer/menu.html` | `foods`, `keyword` | YES |
| `CartController.viewCart()` | `customer/cart.html` | `cart` (CartDto) | YES |
| `CheckoutController.showCheckout()` | `customer/checkout.html` | `cart`, `checkoutDto` | YES |
| `OrderController.orderHistory()` | `customer/orders.html` | `orders` | YES |
| `AdminController.dashboard()` | `admin/dashboard.html` | `summary`, `recentOrders` | YES |
| `AdminFoodController.listFoods()` | `admin/foods.html` | `foods`, `foodDto`, `categories` | YES |
| `AdminInventoryController.list()` | `admin/inventory.html` | `inventoryList`, `lowStockCount` | YES |
| `AdminReportController.profitReport()` | `admin/profit-report.html` | `pl`, `period` | YES |

---

## 6. File Delivery Summary

```
src/main/resources/
├── static/
│   ├── css/style.css                        COMPLETE
│   └── js/app.js                            COMPLETE
└── templates/
    ├── layout/main.html                     COMPLETE
    ├── layout/admin-layout.html             COMPLETE
    ├── auth/login.html                      COMPLETE
    ├── auth/register.html                   COMPLETE
    ├── customer/index.html                  COMPLETE
    ├── customer/menu.html                   COMPLETE
    ├── customer/food-details.html           COMPLETE
    ├── customer/cart.html                   COMPLETE
    ├── customer/checkout.html               COMPLETE
    ├── customer/order-confirmation.html     COMPLETE
    ├── customer/orders.html                 COMPLETE
    ├── customer/order-details.html          COMPLETE
    ├── customer/about.html                  COMPLETE
    ├── customer/contact.html                COMPLETE
    ├── admin/dashboard.html                 COMPLETE
    ├── admin/categories.html                COMPLETE
    ├── admin/foods.html                     COMPLETE
    ├── admin/inventory.html                 COMPLETE
    ├── admin/orders.html                    COMPLETE
    ├── admin/customers.html                 COMPLETE
    ├── admin/expenses.html                  COMPLETE
    ├── admin/sales-report.html              COMPLETE
    ├── admin/profit-report.html             COMPLETE
    ├── error/403.html                       COMPLETE
    └── error/custom-error.html              COMPLETE

docs/06_Frontend_Design.md                   COMPLETE
```

**Total: 21 Thymeleaf templates + 2 static assets (CSS + JS) + 1 documentation file**

---

## 7. Standards Compliance

| Standard | Evidence |
|----------|----------|
| MVC Architecture | Controllers inject Model; views reference only model keys — no business logic in templates |
| SOLID – DIP | Fragment composition via `th:replace` — zero template duplication |
| REST + Form POST | Admin CRUD uses RESTful POST routes (`/admin/foods/add`, `/update/{id}`) |
| CSRF Security | Auto-injected by Spring Security on all POST forms |
| RBAC Guards | `sec:authorize` hides admin links from `ROLE_CUSTOMER` users |
| Responsive Design | Bootstrap 5 grid (`col-md-*`, `col-lg-*`) covers mobile, tablet, desktop |
| UX Flash Feedback | Flash attributes (`successMessage`, `errorMessage`) via `layout/main :: alerts` fragment |
| Accessibility | `aria-*` on modals, `alt` on images, semantic `main`, `nav`, `footer` elements |

---

*Phase 6 Complete — All frontend templates are fully consistent with the database schema,
backend service layer, controllers, DTOs, and REST API contracts defined in Phases 2–5.*
