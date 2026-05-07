# Fooodie Application Development Instructions

This is a plain Java application for an online food delivery service. The project demonstrates object-oriented programming principles with no external dependencies.

## Project Structure

- **Model Layer** (`src/main/java/com/fooodie/model/`): Core entity classes (User, Restaurant, MenuItem, Order, Delivery)
- **Repository Layer** (`src/main/java/com/fooodie/repository/`): Data access interfaces
- **Service Layer** (`src/main/java/com/fooodie/service/`): Business logic implementation
- **Controller Layer** (`src/main/java/com/fooodie/controller/`): Application logic and orchestration
- **Main Class** (`src/main/java/com/fooodie/FooodieApplication.java`): Application entry point

## Compiling the Project

**Windows:**
```bash
mkdir bin
javac -d bin src/main/java/com/fooodie/**/*.java
```

**Linux/Mac:**
```bash
mkdir -p bin
find src/main/java -name "*.java" -type f | xargs javac -d bin
```

## Running the Application

```bash
java -cp bin com.fooodie.FooodieApplication
```

## Key Features

- User management with multiple roles (CUSTOMER, RESTAURANT_OWNER, DELIVERY_PARTNER, ADMIN)
- Restaurant management with owner associations
- Menu item management with pricing and availability
- Order processing and tracking
- Delivery assignment and status updates
- Clean separation of concerns (Model, Service, Repository layers)

## Development Guidelines

- All classes use standard Java patterns
- Service layer handles all business logic and validation
- Repository interfaces define data access contracts
- Model classes represent core business entities
- No external dependencies or frameworks used

## Testing

Create test code in a separate test file or extend the main class to instantiate and test objects. See README.md for usage examples.
