# Fooodie - Online Food Delivery Application

A full-stack online food delivery web application built entirely in **plain Java** — no Spring, no Tomcat, no external frameworks. Uses the built-in JDK HTTP server, SQLite for persistence, and Bootstrap 5 for a modern responsive UI.

---

## Features

- **Login / Register / Logout** — Session-based auth with SHA-256 password hashing
- **Restaurant Browsing** — 6 restaurants with cover photos, ratings, ETA, and delivery fees
- **Menu Browsing** — 24 menu items with food images, categories, and prices
- **Cart Management** — Add/remove items, view subtotals and delivery fee
- **Order Placement** — Checkout and persist orders to SQLite database
- **Order History** — View all past orders with item details and totals
- **Profile Page** — Account info and order statistics
- **Access Control** — All features locked behind login, navbar links hidden until logged in

---

## Prerequisites

You only need **Java JDK 17 or higher** installed. Nothing else.

Check your Java version:
```bash
java -version
```

If Java is not installed, download it from:
👉 https://www.oracle.com/java/technologies/downloads/

---

## How to Run

### Step 1 — Clone the repository

```bash
git clone https://github.com/supreeth208/food-delivery-app.git
cd food-delivery-app
```

---

### Step 2 — Compile

**Windows (Command Prompt):**
```cmd
run.bat
```

**Windows (VS Code PowerShell Terminal):**
```powershell
Get-ChildItem -Recurse -Filter "*.java" src\main\java | % { $_.FullName } | Out-File -Encoding ASCII sources.txt
javac -encoding UTF-8 -d bin -cp "lib\sqlite-jdbc.jar" "@sources.txt"
```

**Linux / Mac (Terminal):**
```bash
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -d bin -cp "lib/sqlite-jdbc.jar" @sources.txt
```

---

### Step 3 — Run

**Windows (VS Code PowerShell / Command Prompt):**
```powershell
java -cp "bin;lib\sqlite-jdbc.jar" com.fooodie.web.HttpServerApp
```

**Linux / Mac:**
```bash
java -cp "bin:lib/sqlite-jdbc.jar" com.fooodie.web.HttpServerApp
```

---

### Step 4 — Open in browser

```
http://localhost:9090
```

You will be redirected to the login page automatically.

> The SQLite database `fooodie.db` is **created automatically** on first run — no setup needed.

---

## Quick Start (Windows — one command)

Open Command Prompt in the project folder and run:
```cmd
run.bat
```
This compiles and starts the server in one step.

---

## Demo Credentials

| Username   | Password      | Role     |
|------------|---------------|----------|
| customer   | customer123   | CUSTOMER |
| admin      | admin123      | ADMIN    |

Or click **Sign Up** on the login page to create a new account.

---

## Available Pages

| URL            | Description                     | Auth Required |
|----------------|---------------------------------|---------------|
| `/`            | Redirects to login or home      | No            |
| `/login`       | Login page                      | No            |
| `/register`    | Register new account            | No            |
| `/home`        | Dashboard with cuisines & stats | Yes           |
| `/restaurants` | Browse all restaurants & menus  | Yes           |
| `/cart`        | View cart and place order       | Yes           |
| `/orders`      | Order history                   | Yes           |
| `/profile`     | Account info and stats          | Yes           |

---

## Project Structure

```
fooodie/
├── src/main/java/com/fooodie/
│   ├── db/                        # Database layer
│   │   ├── Database.java          # SQLite connection + schema init
│   │   ├── AuthDao.java           # User login/register queries
│   │   ├── RestaurantDao.java     # Restaurant + menu queries + seeding
│   │   ├── OrderDao.java          # Order persistence and retrieval
│   │   └── mapper/UserRow.java
│   ├── model/                     # Entity classes
│   │   ├── User.java
│   │   ├── Restaurant.java
│   │   ├── MenuItem.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── Delivery.java
│   ├── services/                  # In-memory stores
│   │   ├── CartStore.java         # Session-based cart
│   │   └── OrderStore.java
│   └── web/
│       ├── HttpServerApp.java     # Server entry point + route wiring
│       ├── handlers/              # One class per route
│       ├── middleware/            # Router, RouteHandler, AuthMiddleware
│       ├── req/                   # RequestContext (params, cookies)
│       ├── resp/                  # Response builder
│       ├── session/               # SessionManager, UserSession
│       └── template/              # HtmlPage layout
├── lib/
│   └── sqlite-jdbc.jar            # Only external dependency (included)
├── bin/                           # Compiled .class files (auto-generated)
├── fooodie.db                     # SQLite database (auto-created on first run)
├── run.bat                        # Windows compile + run script
└── PROJECT_REPORT.md              # Full project report
```

---

## Technologies Used

| Layer       | Technology                          |
|-------------|-------------------------------------|
| Language    | Java 17                             |
| HTTP Server | `com.sun.net.httpserver` (built-in) |
| Database    | SQLite via `sqlite-jdbc`            |
| Frontend    | Bootstrap 5.3, Google Fonts (Inter) |
| Auth        | SHA-256 + UUID session cookies      |
| Build       | `javac` (no Maven/Gradle)           |

---

## Database

The SQLite database `fooodie.db` is **automatically created** on first run with:
- 6 seeded restaurants
- 24 menu items
- 2 demo user accounts

Tables: `users`, `restaurants`, `menu_items`, `orders`, `order_items`

---

## Restaurants

| # | Name             | Cuisine  | Items | Rating |
|---|------------------|----------|-------|--------|
| 1 | Pizza Palace     | Italian  | 4     | 4.6    |
| 2 | Sushi World      | Japanese | 4     | 4.8    |
| 3 | Burger Barn      | American | 4     | 4.5    |
| 4 | Spice Garden     | Indian   | 4     | 4.7    |
| 5 | Taco Fiesta      | Mexican  | 4     | 4.4    |
| 6 | The Pasta House  | Italian  | 4     | 4.6    |

---

## Troubleshooting

**Port already in use:**
```powershell
# Windows — find and kill the process using port 9090
netstat -ano | findstr :9090
taskkill /PID <PID> /F
```

**`NoClassDefFoundError` on startup:**
Make sure you include the SQLite jar in the classpath:
```powershell
java -cp "bin;lib\sqlite-jdbc.jar" com.fooodie.web.HttpServerApp   # Windows
java -cp "bin:lib/sqlite-jdbc.jar" com.fooodie.web.HttpServerApp   # Linux/Mac
```

**Login page hangs / not redirecting:**
The server was started without the SQLite jar. Stop and restart using the exact command above.

**Recompile after making changes:**
```powershell
Get-ChildItem -Recurse -Filter "*.java" src\main\java | % { $_.FullName } | Out-File -Encoding ASCII sources.txt
javac -encoding UTF-8 -d bin -cp "lib\sqlite-jdbc.jar" "@sources.txt"
java -cp "bin;lib\sqlite-jdbc.jar" com.fooodie.web.HttpServerApp
```

---

## Future Enhancements

- Real-time order tracking
- Search and filter restaurants
- Payment integration
- Admin panel for CRUD operations
- Email order confirmations
- Docker deployment

---

## License

This project is open source and available under the MIT License.
