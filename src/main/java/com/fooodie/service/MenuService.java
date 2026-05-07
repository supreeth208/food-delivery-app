package com.fooodie.service;

import com.fooodie.model.MenuItem;
import com.fooodie.repository.MenuItemRepository;
import java.util.List;
import java.util.Optional;

/**
 * MenuService for managing MenuItem business logic
 */
public class MenuService {
    
    private MenuItemRepository menuItemRepository;

    public MenuService() {
        this.menuItemRepository = new MenuItemRepository();
    }

    public MenuService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> getMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItem> getMenuByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public List<MenuItem> getAvailableMenuByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId);
    }

    public MenuItem updateMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public void toggleMenuItemAvailability(Long menuItemId) {
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);
        menuItem.ifPresent(m -> {
            m.setAvailable(!m.getAvailable());
            menuItemRepository.save(m);
        });
    }

    public void deleteMenuItem(Long id) {
        menuItemRepository.delete(id);
    }
}

