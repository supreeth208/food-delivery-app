package com.fooodie.repository;

import com.fooodie.model.MenuItem;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MenuItemRepository for managing MenuItem data
 * This is an in-memory implementation for demonstration purposes
 */
public class MenuItemRepository {
    
    private static final Map<Long, MenuItem> menuItems = new HashMap<>();
    private static long idCounter = 1;

    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() == null) {
            menuItem.setId(idCounter++);
        }
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }

    public Optional<MenuItem> findById(Long id) {
        return Optional.ofNullable(menuItems.get(id));
    }

    public List<MenuItem> findAll() {
        return new ArrayList<>(menuItems.values());
    }

    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return menuItems.values().stream()
                .filter(m -> m.getRestaurant().getId().equals(restaurantId))
                .collect(Collectors.toList());
    }

    public List<MenuItem> findByRestaurantIdAndAvailableTrue(Long restaurantId) {
        return menuItems.values().stream()
                .filter(m -> m.getRestaurant().getId().equals(restaurantId) && m.getAvailable())
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        menuItems.remove(id);
    }

    public void deleteAll() {
        menuItems.clear();
    }

    public long count() {
        return menuItems.size();
    }
}

