package com.fooodie.repository;

import com.fooodie.model.Delivery;
import java.util.*;

/**
 * DeliveryRepository for managing Delivery data
 * This is an in-memory implementation for demonstration purposes
 */
public class DeliveryRepository {
    
    private static final Map<Long, Delivery> deliveries = new HashMap<>();
    private static long idCounter = 1;

    public Delivery save(Delivery delivery) {
        if (delivery.getId() == null) {
            delivery.setId(idCounter++);
        }
        deliveries.put(delivery.getId(), delivery);
        return delivery;
    }

    public Optional<Delivery> findById(Long id) {
        return Optional.ofNullable(deliveries.get(id));
    }

    public List<Delivery> findAll() {
        return new ArrayList<>(deliveries.values());
    }

    public Optional<Delivery> findByOrderId(Long orderId) {
        return deliveries.values().stream()
                .filter(d -> d.getOrder().getId().equals(orderId))
                .findFirst();
    }

    public void delete(Long id) {
        deliveries.remove(id);
    }

    public void deleteAll() {
        deliveries.clear();
    }

    public long count() {
        return deliveries.size();
    }
}

