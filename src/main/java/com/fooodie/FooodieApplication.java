package com.fooodie;

import com.fooodie.model.*;
import com.fooodie.service.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for Fooodie - Online Food Delivery Service
 * This demonstrates basic usage of the application's classes and services
 */
public class FooodieApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Welcome to Fooodie - Food Delivery App");
        System.out.println("========================================\n");

        // Initialize services
        UserService userService = new UserService();
        RestaurantService restaurantService = new RestaurantService();
        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService();
        DeliveryService deliveryService = new DeliveryService();

        // Example: Create users
        System.out.println("--- Creating Users ---");
        User restaurantOwner = new User(
            1L,
            "john_restaurant",
            "john@pizzapalace.com",
            "password123",
            "John Smith",
            "555-0123",
            "456 Restaurant Ave",
            User.UserRole.RESTAURANT_OWNER,
            true
        );
        System.out.println("Created restaurant owner: " + restaurantOwner.getFullName());

        User customer = new User(
            2L,
            "jane_customer",
            "jane@email.com",
            "password456",
            "Jane Doe",
            "555-0456",
            "123 Customer St",
            User.UserRole.CUSTOMER,
            true
        );
        System.out.println("Created customer: " + customer.getFullName());

        User deliveryPartner = new User(
            3L,
            "bob_delivery",
            "bob@delivery.com",
            "password789",
            "Bob Johnson",
            "555-0789",
            "789 Delivery Lane",
            User.UserRole.DELIVERY_PARTNER,
            true
        );
        System.out.println("Created delivery partner: " + deliveryPartner.getFullName());

        // Example: Create restaurant
        System.out.println("\n--- Creating Restaurant ---");
        Restaurant restaurant = new Restaurant(
            1L,
            "Pizza Palace",
            "The best pizza in the city!",
            "456 Restaurant Ave",
            "555-PIZZA",
            4.5,
            2.99,
            30,
            restaurantOwner,
            new ArrayList<>(),
            true,
            true
        );
        System.out.println("Created restaurant: " + restaurant.getName());
        System.out.println("Owner: " + restaurant.getOwner().getFullName());

        // Example: Add menu items
        System.out.println("\n--- Adding Menu Items ---");
        MenuItem margherita = new MenuItem(
            1L,
            "Margherita Pizza",
            "Classic pizza with tomato, mozzarella, and basil",
            12.99,
            "Pizza",
            true,
            restaurant,
            "https://example.com/margherita.jpg",
            15
        );
        System.out.println("Added: " + margherita.getName() + " - $" + margherita.getPrice());

        MenuItem pepperoni = new MenuItem(
            2L,
            "Pepperoni Pizza",
            "Pizza with pepperoni and mozzarella",
            14.99,
            "Pizza",
            true,
            restaurant,
            "https://example.com/pepperoni.jpg",
            15
        );
        System.out.println("Added: " + pepperoni.getName() + " - $" + pepperoni.getPrice());

        // Example: Create order items
        System.out.println("\n--- Creating Order ---");
        List<OrderItem> orderItems = new ArrayList<>();

        OrderItem item1 = new OrderItem(
            1L,
            null, // Will be set with order
            margherita,
            2,
            margherita.getPrice(),
            "Extra cheese please"
        );
        orderItems.add(item1);

        OrderItem item2 = new OrderItem(
            2L,
            null, // Will be set with order
            pepperoni,
            1,
            pepperoni.getPrice(),
            "No onions"
        );
        orderItems.add(item2);

        // Example: Create order
        Order order = new Order(
            1L,
            customer,
            restaurant,
            orderItems,
            50.96,
            2.99,
            Order.OrderStatus.PENDING,
            "123 Customer St, Apt 4B",
            null,
            null,
            null,
            null
        );

        // Set order reference for order items
        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
        }

        System.out.println("Order created for: " + order.getCustomer().getFullName());
        System.out.println("Restaurant: " + order.getRestaurant().getName());
        System.out.println("Number of items: " + order.getOrderItems().size());
        System.out.println("Order Total: $" + order.getTotalAmount());
        System.out.println("Status: " + order.getStatus());

        // Example: Create delivery
        System.out.println("\n--- Assigning Delivery ---");
        Delivery delivery = new Delivery(
            1L,
            order,
            deliveryPartner,
            Delivery.DeliveryStatus.ASSIGNED,
            "Pizza Palace",
            "123 Customer St, Apt 4B",
            null,
            null,
            30
        );
        System.out.println("Delivery assigned to: " + delivery.getDeliveryPartner().getFullName());
        System.out.println("Estimated time: " + delivery.getEstimatedMinutes() + " minutes");
        System.out.println("Status: " + delivery.getStatus());

        // Summary
        System.out.println("\n========================================");
        System.out.println("Application Demo Complete!");
        System.out.println("========================================");
    }
}

