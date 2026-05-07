package com.fooodie.repository;

import com.fooodie.model.Order;
import java.util.*;
import java.util.stream.Collectors;

/**
 * OrderRepository for managing Order data
 * This is an in-memory implementation for demonstration purposes
 */
public class OrderRepository {
    
    private static final Map<Long, Order> orders = new HashMap<>();
    private static long idCounter = 1;

    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(idCounter++);
        }
        orders.put(order.getId(), order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public List<Order> findByCustomerId(Long customerId) {
        return orders.values().stream()
                .filter(o -> o.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Order> findByRestaurantId(Long restaurantId) {
        return orders.values().stream()
                .filter(o -> o.getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    public List<Order> findByDeliveryPartnerId(Long deliveryPartnerId) {
        return orders.values().stream()
                .filter(o -> o.getDeliveryPartner() != null && o.getDeliveryPartner().getId().equals(deliveryPartnerId))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        orders.remove(id);
    }

    public void deleteAll() {
        orders.clear();
    }

    public long count() {
        return orders.size();
    }
}

