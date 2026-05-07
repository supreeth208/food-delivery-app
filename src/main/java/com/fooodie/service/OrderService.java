package com.fooodie.service;

import com.fooodie.model.Order;
import com.fooodie.model.OrderItem;
import com.fooodie.repository.OrderRepository;
import java.util.List;
import java.util.Optional;

/**
 * OrderService for managing Order business logic
 */
public class OrderService {
    
    private OrderRepository orderRepository;

    public OrderService() {
        this.orderRepository = new OrderRepository();
    }

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    public List<Order> getOrdersByDeliveryPartner(Long deliveryPartnerId) {
        return orderRepository.findByDeliveryPartnerId(deliveryPartnerId);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public void updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.setStatus(status);
            orderRepository.save(o);
        });
    }

    public void assignDeliveryPartner(Long orderId, Long deliveryPartnerId) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.getDeliveryPartner().setId(deliveryPartnerId);
            o.setStatus(Order.OrderStatus.OUT_FOR_DELIVERY);
            orderRepository.save(o);
        });
    }

    public Double calculateOrderTotal(Order order) {
        double itemsTotal = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getSubtotal)
                .sum();
        return itemsTotal + order.getDeliveryFee();
    }

    public void deleteOrder(Long id) {
        orderRepository.delete(id);
    }
}

