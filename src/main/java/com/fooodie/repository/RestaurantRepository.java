package com.fooodie.repository;

import com.fooodie.model.Restaurant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RestaurantRepository for managing Restaurant data
 * This is an in-memory implementation for demonstration purposes
 */
public class RestaurantRepository {
    
    private static final Map<Long, Restaurant> restaurants = new HashMap<>();
    private static long idCounter = 1;

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idCounter++);
        }
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public Optional<Restaurant> findById(Long id) {
        return Optional.ofNullable(restaurants.get(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }

    public List<Restaurant> findByIsOpenTrue() {
        return restaurants.values().stream()
                .filter(Restaurant::getIsOpen)
                .collect(Collectors.toList());
    }

    public List<Restaurant> findByOwnerIdAndActiveTrue(Long ownerId) {
        return restaurants.values().stream()
                .filter(r -> r.getOwner().getId().equals(ownerId) && r.getActive())
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        restaurants.remove(id);
    }

    public void deleteAll() {
        restaurants.clear();
    }

    public long count() {
        return restaurants.size();
    }
}

