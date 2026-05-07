package com.fooodie.service;

import com.fooodie.model.Restaurant;
import com.fooodie.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;

/**
 * RestaurantService for managing Restaurant business logic
 */
public class RestaurantService {
    
    private RestaurantRepository restaurantRepository;

    public RestaurantService() {
        this.restaurantRepository = new RestaurantRepository();
    }

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getOpenRestaurants() {
        return restaurantRepository.findByIsOpenTrue();
    }

    public List<Restaurant> getRestaurantsByOwner(Long ownerId) {
        return restaurantRepository.findByOwnerIdAndActiveTrue(ownerId);
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void toggleRestaurantStatus(Long restaurantId) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        restaurant.ifPresent(r -> {
            r.setIsOpen(!r.getIsOpen());
            restaurantRepository.save(r);
        });
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.delete(id);
    }
}

