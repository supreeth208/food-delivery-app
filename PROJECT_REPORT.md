# Fooodie — Online Food Delivery Application
## Project Report

---

## Table of Contents

1. Project Description
2. Tools and Technologies Used
3. Methodology
4. Implementation
5. Results
6. Challenges
7. Conclusion

---

## 1. Project Description

### Overview

Fooodie is a full-stack online food delivery web application built entirely in plain Java — without any external frameworks such as Spring Boot or Jakarta EE. The application allows customers to browse restaurants, explore menus, add items to a cart, place orders, and view their order history through a modern, responsive web interface.

The project was designed as an educational demonstration of how a complete, production-like web application can be built using only core Java features, the built-in JDK HTTP server, JDBC, and SQLite — proving that a functional full-stack system does not require heavyweight frameworks.

### Objectives

- Build a working food delivery platform from scratch using plain Java
- Implement a layered architecture (model, service, repository, web)
- Provide a clean, attractive UI using Bootstrap 5 served as HTML from Java handlers
- Persist all data (users, restaurants, menus, orders) in a SQLite database
- Implement session-based authentication with login/register/logout
- Enforce access control — all features locked behind login

### Scope

The application covers the complete customer-facing flow:

| Feature              | Description                                              |
|----------------------|----------------------------------------------------------|
| User Registration    | New customers can create an account                      |
| User Login / Logout  | Session-based authentication with SHA-256 password hash  |
| Restaurant Browsing  | View all restaurants with ratings, ETA, and delivery fee |
| Menu Browsing        | View food items with images, descriptions, and prices    |
| Cart Management      | Add/remove items, view subtotals                         |
| Order Placement      | Checkout cart and persist order to database              |
| Order History        | View all past orders with item details and totals        |
| Profile Page         | View account info and order statistics                   |

---

## 2. Tools and Technologies Used

### Programming Language

| Tool        | Version  | Purpose                          |
|-------------|----------|----------------------------------|
| Java (JDK)  | 17 LTS   | Core application language        |

### Backend

| Component                        | Description                                                  |
|----------------------------------|--------------------------------------------------------------|
| `com.sun.net.httpserver`         | Built-in JDK HTTP server — no external server needed         |
| JDBC (Java Database Connectivity)| Standard Java API for database access                        |
| SQLite via `sqlite-jdbc` 3.45.3  | Lightweight embedded relational database                     |
| SHA-256 (`java.security`)        | Password hashing for secure credential storage               |
| `ConcurrentHashMap`              | In-memory session store for user sessions                    |
| `UUID`                           | Unique session token generation                              |

### Frontend

| Component      | Version | Purpose                                      |
|----------------|---------|----------------------------------------------|
| Bootstrap      | 5.3.3   | Responsive CSS framework (via CDN)           |
| Google Fonts   | Inter   | Modern sans-serif typeface                   |
| Custom CSS     | —       | CSS variables, animations, card hover effects|
| HTML5          | —       | Page structure rendered server-side in Java  |

### Database

| Component   | Description                                                       |
|-------------|-------------------------------------------------------------------|
| SQLite      | Embedded file-based database stored as `fooodie.db`               |
| Tables      | `users`, `restaurants`, `menu_items`, `orders`, `order_items`     |
| Auto-seed   | Database is created and seeded with 6 restaurants on first run    |

### Development Tools

| Tool              | Purpose                              |
|-------------------|--------------------------------------|
| `javac`           | Java compiler                        |
| `java`            | JVM runtime                          |
| `run.bat`         | Windows batch script to compile & run|
| Git               | Version control                      |
| Amazon Q          | AI-assisted development              |

---

## 3. Methodology

### Development Approach

The project followed an **iterative, bottom-up development approach**:

```
Phase 1 → Model Layer       (User, Restaurant, MenuItem, Order, OrderItem, Delivery)
Phase 2 → Database Layer    (SQLite schema, DAOs, seeding)
Phase 3 → Web Layer         (HTTP server, router, request/response abstractions)
Phase 4 → Handlers          (One handler class per route)
Phase 5 → UI Design         (Bootstrap layout, CSS variables, responsive design)
Phase 6 → Integration       (Connect all layers end-to-end)
Phase 7 → Auth & Guards     (Login-first flow, session management)
```

### Architecture

The application follows a clean **MVC-inspired layered architecture**:

```
┌─────────────────────────────────────────────┐
│                  Browser                     │
└──────────────────────┬──────────────────────┘
                       │ HTTP
┌──────────────────────▼──────────────────────┐
│              Web Layer (Handlers)            │
│  HomeHandler, RestaurantsHandler,            │
│  CartHandler, OrdersHandler, etc.            │
└──────────────────────┬──────────────────────┘
                       │
┌──────────────────────▼──────────────────────┐
│            Services / Stores                 │
│  CartStore (in-memory), OrderStore           │
│  InMemoryCatalog (seeding bridge)            │
└──────────────────────┬──────────────────────┘
                       │
┌──────────────────────▼──────────────────────┐
│              DAO Layer (DB)                  │
│  AuthDao, RestaurantDao, OrderDao            │
└──────────────────────┬──────────────────────┘
                       │ JDBC
┌──────────────────────▼──────────────────────┐
│           SQLite Database (fooodie.db)       │
│  users, restaurants, menu_items,             │
│  orders, order_items                         │
└─────────────────────────────────────────────┘
```

### Database Design

**Entity Relationship Overview:**

```
users ──────────────── orders ──────────── order_items
                          │                     │
                    restaurants ──── menu_items ─┘
```

**Schema:**

```sql
users        (id, username, password_hash, role, full_name, email, active)
restaurants  (id, name, description, address, phone, rating, delivery_fee, eta_minutes, is_open, cover_url)
menu_items   (id, restaurant_id, name, description, price, category, image_url, prep_time, available)
orders       (id, username, restaurant_id, status, subtotal, delivery_fee, total, created_at)
order_items  (id, order_id, menu_item_id, name, price, quantity)
```

### Authentication Flow

```
User visits any URL
        │
        ▼
  Session cookie present?
     No  ──► Redirect to /login
     Yes ──► Allow access
        │
        ▼
  POST /login
  SHA-256(password) == stored hash?
     No  ──► Show error on login page
     Yes ──► Create session token (UUID)
           ──► Set FOOODIE_SESSION cookie
           ──► Redirect to /home (dashboard)
```

---

## 4. Implementation

### Project Structure

```
fooodie/
├── src/main/java/com/fooodie/
│   ├── db/
│   │   ├── Database.java          # SQLite connection + schema init
│   │   ├── AuthDao.java           # User CRUD (login, register)
│   │   ├── RestaurantDao.java     # Restaurant + menu item queries + seeding
│   │   ├── OrderDao.java          # Order persistence and retrieval
│   │   └── mapper/
│   │       └── UserRow.java       # Record for DB user row
│   ├── model/
│   │   ├── User.java              # User entity with roles
│   │   ├── Restaurant.java        # Restaurant entity
│   │   ├── MenuItem.java          # Menu item entity
│   │   ├── Order.java             # Order entity with status enum
│   │   ├── OrderItem.java         # Individual order line item
│   │   └── Delivery.java          # Delivery tracking entity
│   ├── services/
│   │   ├── CartStore.java         # In-memory cart (session → items)
│   │   ├── OrderStore.java        # In-memory order cache
│   │   └── InMemoryCatalog.java   # Catalog bridge (legacy)
│   ├── web/
│   │   ├── HttpServerApp.java     # Server entry point + route wiring
│   │   ├── handlers/              # One class per route
│   │   │   ├── HomeHandler.java
│   │   │   ├── DashboardHandler.java
│   │   │   ├── LoginPageHandler.java
│   │   │   ├── LoginHandler.java
│   │   │   ├── RegisterPageHandler.java
│   │   │   ├── RegisterHandler.java
│   │   │   ├── LogoutHandler.java
│   │   │   ├── RestaurantsHandler.java
│   │   │   ├── CartHandler.java
│   │   │   ├── CartAddHandler.java
│   │   │   ├── CartRemoveHandler.java
│   │   │   ├── CheckoutHandler.java
│   │   │   ├── OrdersHandler.java
│   │   │   └── ProfileHandler.java
│   │   ├── middleware/
│   │   │   ├── Router.java        # URL pattern matching + dispatch
│   │   │   ├── RouteHandler.java  # Functional interface for handlers
│   │   │   └── AuthMiddleware.java
│   │   ├── req/
│   │   │   └── RequestContext.java # Query/form param + cookie parsing
│   │   ├── resp/
│   │   │   └── Response.java      # HTTP response builder
│   │   ├── session/
│   │   │   ├── SessionManager.java # UUID token → UserSession map
│   │   │   └── UserSession.java
│   │   └── template/
│   │       └── HtmlPage.java      # Global HTML layout with navbar
├── lib/
│   └── sqlite-jdbc.jar            # Only external dependency
├── bin/                           # Compiled .class files
├── fooodie.db                     # SQLite database (auto-created)
├── run.bat                        # Compile + run script
└── README.md
```

### Key Implementation Details

#### HTTP Server (No Framework)
The application uses Java's built-in `com.sun.net.httpserver.HttpServer`. A custom `Router` class matches incoming requests by method + path pattern and dispatches to the appropriate `RouteHandler`.

```java
Router router = new Router(9090);
router.get("/restaurants", new RestaurantsHandler());
router.post("/cart/add",   new CartAddHandler());
router.start();
```

#### Database Initialization
`Database.java` uses a static initializer to load the SQLite driver and create all tables on first run using `CREATE TABLE IF NOT EXISTS`. `RestaurantDao` seeds 6 restaurants and 24 menu items if the table is empty.

#### Session Management
Sessions are stored in a `ConcurrentHashMap<String, UserSession>` keyed by a UUID token. The token is sent to the browser as an `HttpOnly` cookie (`FOOODIE_SESSION`). Every protected handler calls `SessionManager.getSession(ctx)` and redirects to `/login` if null.

#### Cart
The cart is stored in-memory in `CartStore` — a `ConcurrentHashMap` keyed by session token, mapping menu item IDs to `CartItem` objects. On checkout, the cart is converted to an `Order` and persisted to SQLite, then cleared.

#### UI Rendering
All HTML is generated server-side in Java handler classes using `StringBuilder`. The global layout (navbar, CSS, Bootstrap CDN links) is provided by `HtmlPage.layout()`. The navbar dynamically shows/hides links based on whether the user is logged in.

### Routes Summary

| Method | Path          | Handler                | Auth Required |
|--------|---------------|------------------------|---------------|
| GET    | /             | HomeHandler            | No (redirects)|
| GET    | /login        | LoginPageHandler       | No            |
| POST   | /login        | LoginHandler           | No            |
| GET    | /register     | RegisterPageHandler    | No            |
| POST   | /register     | RegisterHandler        | No            |
| GET    | /logout       | LogoutHandler          | No            |
| GET    | /home         | DashboardHandler       | Yes           |
| GET    | /restaurants  | RestaurantsHandler     | Yes           |
| POST   | /cart/add     | CartAddHandler         | Yes           |
| POST   | /cart/remove  | CartRemoveHandler      | Yes           |
| GET    | /cart         | CartHandler            | Yes           |
| POST   | /checkout     | CheckoutHandler        | Yes           |
| GET    | /orders       | OrdersHandler          | Yes           |
| GET    | /profile      | ProfileHandler         | Yes           |

---

## 5. Results

### Functional Outcomes

The application successfully delivers all planned features:

- **Authentication** — Users can register, log in, and log out. Passwords are stored as SHA-256 hashes. All protected routes redirect unauthenticated users to the login page.

- **Restaurant Catalog** — 6 restaurants with 4 menu items each (24 total) are seeded into SQLite on first run and served dynamically from the database on every request.

- **Cart System** — Users can add multiple items from a restaurant, view the cart with subtotals and a delivery fee summary, remove items, and proceed to checkout.

- **Order Persistence** — Placed orders are written to the `orders` and `order_items` tables in SQLite. Orders survive server restarts and are retrieved per user from the database.

- **Order History** — The orders page loads all past orders for the logged-in user from SQLite, displaying item images, quantities, subtotals, and order status badges.

- **Profile Page** — Displays username, role, total order count (live from DB), and cart item count.

### Restaurants in the System

| # | Restaurant       | Cuisine  | Items | Rating | Delivery Fee |
|---|------------------|----------|-------|--------|--------------|
| 1 | Pizza Palace     | Italian  | 4     | 4.6    | $2.99        |
| 2 | Sushi World      | Japanese | 4     | 4.8    | $3.49        |
| 3 | Burger Barn      | American | 4     | 4.5    | $1.99        |
| 4 | Spice Garden     | Indian   | 4     | 4.7    | $2.49        |
| 5 | Taco Fiesta      | Mexican  | 4     | 4.4    | $1.49        |
| 6 | The Pasta House  | Italian  | 4     | 4.6    | $2.99        |

### UI Highlights

- Dark gradient hero section on the dashboard
- Restaurant cards with cover photos, star ratings, open/ETA badges
- Menu item cards with food photography, category pills, hover effects
- Split-panel login/register pages with demo credentials
- Cart with order summary sidebar
- Color-coded order status badges
- Responsive layout (Bootstrap 5 grid)
- Smooth fade-up animation on page load

### Demo Credentials

| Username   | Password      | Role     |
|------------|---------------|----------|
| admin      | admin123      | ADMIN    |
| customer   | customer123   | CUSTOMER |

---

## 6. Challenges

### Challenge 1 — No Framework, No Magic
Building without Spring Boot meant implementing everything manually: URL routing, request parsing, response writing, session cookies, form decoding, and HTML rendering. Each piece required understanding the underlying HTTP protocol.

**Solution:** Built a lightweight `Router` class using `com.sun.net.httpserver`, a `RequestContext` helper for parsing query/form params and cookies, and a `Response` builder for HTML/redirect responses.

### Challenge 2 — MySQL Driver Not Available
The original codebase was wired to MySQL. Without a running MySQL server or driver JAR, the application crashed on every login/register attempt with `No suitable driver found`.

**Solution:** Replaced MySQL with SQLite using the `sqlite-jdbc` standalone JAR (no server required). The database file `fooodie.db` is created automatically on first run.

### Challenge 3 — Windows Classpath Issues
The SQLite JAR was on the classpath during compilation but not at runtime, causing `NoClassDefFoundError: Could not initialize class com.fooodie.db.Database`. Windows uses `;` as the classpath separator, and `Start-Process` in PowerShell splits arguments on spaces.

**Solution:** Created `run.bat` which uses the correct Windows classpath syntax (`bin;lib\sqlite-jdbc.jar`) for both compilation and execution.

### Challenge 4 — Java Compiler Encoding Errors
Emoji characters (⭐, 🚚, ⏱) in source files caused `unmappable character` errors on Windows because the default compiler encoding is `windows-1252`, not UTF-8.

**Solution:** Replaced all emoji with plain ASCII text and added the `-encoding UTF-8` flag to all `javac` commands.

### Challenge 5 — Text Block Parse Errors
The `"""` string in `escapeHtml` methods was being parsed as a Java text block opener, causing compile errors.

**Solution:** Replaced `"""` with the HTML entity string `"&quot;"` to avoid the ambiguity.

### Challenge 6 — HTTP Header Type Mismatch
`HttpExchange.getResponseHeaders().putAll()` expects `Map<String, List<String>>` but `Response.headers` was `Map<String, String>`, causing a compile-time type error.

**Solution:** Replaced `putAll()` with `forEach((k, v) -> exchange.getResponseHeaders().set(k, v))`.

### Challenge 7 — Port 8080 Blocked by Windows
Windows HTTP.sys reserves port 8080, causing 403 Forbidden responses even though the server started successfully.

**Solution:** Switched the server port to `9090`.

---

## 7. Conclusion

Fooodie demonstrates that a complete, functional, and visually attractive full-stack web application can be built using only plain Java — no Spring, no Hibernate, no Tomcat. The project covers the full software development lifecycle from requirements and architecture through implementation, debugging, and deployment.

### Key Takeaways

- **Java is sufficient** — The JDK provides everything needed: an HTTP server, JDBC, cryptography, collections, and concurrency utilities.
- **Layered architecture matters** — Separating models, DAOs, services, and web handlers made the codebase maintainable and easy to extend.
- **SQLite is ideal for embedded apps** — Zero configuration, no server process, single file, full SQL support.
- **UI quality is achievable without JavaScript frameworks** — Bootstrap 5 with custom CSS variables delivers a modern, responsive interface from pure server-side HTML.

### Future Enhancements

| Enhancement                  | Description                                              |
|------------------------------|----------------------------------------------------------|
| Real-time order tracking     | WebSocket-based live status updates                      |
| Search & filter              | Filter restaurants by cuisine, rating, or delivery time  |
| Payment integration          | Stripe or PayPal checkout flow                           |
| Admin panel                  | CRUD for restaurants, menus, and order management        |
| Email notifications          | Order confirmation emails via JavaMail                   |
| Image upload                 | Allow restaurant owners to upload menu item photos       |
| Unit tests                   | JUnit 5 test suite for DAOs and service logic            |
| Docker deployment            | Containerise the app with the SQLite DB volume mounted   |

---

*Report prepared for the Fooodie project — Plain Java Full-Stack Food Delivery Application*
*Technology: Java 17 | SQLite | Bootstrap 5 | No external frameworks*
