# Fooodie - Online Food Delivery Application

A basic online food delivery application built with plain Java. This application demonstrates core object-oriented programming concepts with model classes for managing users, restaurants, menus, orders, and deliveries.

## Features

- **User Management**: Classes for managing users (customers, restaurant owners, delivery partners, admins)
- **Restaurant Management**: Restaurant management with owner associations
- **Menu Management**: Menu item management with pricing and availability
- **Order Processing**: Order creation and tracking system
- **Delivery Tracking**: Delivery management with status updates
- **Object-Oriented Design**: Clean architecture with model, service, and controller layers

## Project Structure

```
fooodie/
├── src/main/java/com/fooodie/
│   ├── controller/          # API endpoint classes
│   ├── model/              # Entity/Model classes
│   ├── service/            # Business logic layer
│   ├── repository/         # Data access interfaces
│   └── FooodieApplication.java  # Main application class
├── src/main/resources/
│   └── application.properties   # Configuration file
└── README.md              # This file
```

## Technologies Used

- **Java 11+**: Programming language
- **Object-Oriented Programming**: Core design patterns
- **Data Structures**: Collections for managing data
- **File I/O**: For potential data persistence

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Git (optional)

## Installation and Setup

1. **Download the project**
   ```bash
   cd fooodie
   ```

2. **Compile the Java source files**
   ```bash
   javac -d bin src/main/java/com/fooodie/**/*.java
   ```

3. **Run the application**
   ```bash
   java -cp bin com.fooodie.FooodieApplication
   ```

## API Endpoints

This plain Java implementation focuses on core business logic classes. The classes can be used as follows:

### User Class
```java
User user = new User(
    "john_doe",
    "john@example.com",
    "password123",
    "John Doe",
    "555-0123",
    "123 Main St",
    User.UserRole.CUSTOMER
);
```

### Restaurant Class
```java
Restaurant restaurant = new Restaurant(
    "Pizza Palace",
    "Best pizza in town",
    "456 Oak Ave",
    "555-9876",
    2.99,
    30
);
restaurant.setOwner(owner);
```

### Menu Item Class
```java
MenuItem item = new MenuItem(
    "Margherita Pizza",
    "Classic pizza with tomato and mozzarella",
    12.99,
    "Pizza",
    15
);
item.setRestaurant(restaurant);
```

### Order Class
```java
Order order = new Order(
    customer,
    restaurant,
    orderItems,
    deliveryAddress
);
order.calculateTotal();
```

## Core Classes

- **User**: Represents users with different roles (CUSTOMER, RESTAURANT_OWNER, DELIVERY_PARTNER, ADMIN)
- **Restaurant**: Manages restaurant information and associated menu items
- **MenuItem**: Represents food items available in restaurants
- **Order**: Tracks customer orders with multiple items
- **OrderItem**: Individual items in an order with quantity and price
- **Delivery**: Manages delivery tracking and status

## Service Layer

Service classes provide business logic:
- `UserService`: User management operations
- `RestaurantService`: Restaurant management
- `MenuService`: Menu item operations
- `OrderService`: Order processing
- `DeliveryService`: Delivery tracking

## Example Usage

```java
// Create users
User owner = new User(...);
User customer = new User(...);

// Create restaurant
Restaurant restaurant = new Restaurant(...);
restaurant.setOwner(owner);

// Add menu items
MenuItem pizza = new MenuItem("Pizza", "desc", 12.99, "Pizza", 15);
pizza.setRestaurant(restaurant);

// Create order
Order order = new Order(customer, restaurant, items, "delivery address");
order.setStatus(Order.OrderStatus.PENDING);
```

## Future Enhancements

- Add file-based or database persistence (JDBC/SQLite)
- Implement a simple console or GUI interface (Swing/JavaFX)
- Add input validation and error handling
- Implement data serialization (JSON)
- Add logging functionality
- Create unit tests with JUnit
- Build a REST API wrapper (if needed)

## Building and Running Manually

### On Windows:
```bash
# Create bin directory
mkdir bin

# Compile all Java files
javac -d bin src/main/java/com/fooodie/**/*.java

# Run the application
java -cp bin com.fooodie.FooodieApplication
```

### On Linux/Mac:
```bash
# Create bin directory
mkdir -p bin

# Compile all Java files
find src/main/java -name "*.java" -type f | xargs javac -d bin

# Run the application
java -cp bin com.fooodie.FooodieApplication
```

## Notes

- This is a **plain Java implementation** focused on object-oriented design principles
- No external dependencies or frameworks required
- All classes use standard Java features
- Model classes can be easily integrated with databases or APIs later
- Service layer provides clean separation of concerns

## Contributing

Feel free to fork this project and submit improvements.

## License

This project is open source and available under the MIT License.

## Support

For issues or questions, please create an issue in the project repository.

---

**Note**: This is a basic educational implementation demonstrating OOP concepts and application architecture in plain Java.
