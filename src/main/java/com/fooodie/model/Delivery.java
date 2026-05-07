package com.fooodie.model;

import java.time.LocalDateTime;

/**
 * Delivery class representing delivery management in the Fooodie application
 */
public class Delivery {
    
    private Long id;
    private Order order;
    private User deliveryPartner;
    private DeliveryStatus status;
    private String currentLocation;
    private String destinationLocation;
    private LocalDateTime pickupTime;
    private LocalDateTime deliveryTime;
    private Integer estimatedMinutes;

    public enum DeliveryStatus {
        ASSIGNED, PICKED_UP, IN_TRANSIT, ARRIVED, COMPLETED
    }

    // Constructors
    public Delivery() {
        this.pickupTime = LocalDateTime.now();
    }

    public Delivery(Long id, Order order, User deliveryPartner, DeliveryStatus status,
                   String currentLocation, String destinationLocation, 
                   LocalDateTime pickupTime, LocalDateTime deliveryTime, Integer estimatedMinutes) {
        this.id = id;
        this.order = order;
        this.deliveryPartner = deliveryPartner;
        this.status = status;
        this.currentLocation = currentLocation;
        this.destinationLocation = destinationLocation;
        this.pickupTime = pickupTime != null ? pickupTime : LocalDateTime.now();
        this.deliveryTime = deliveryTime;
        this.estimatedMinutes = estimatedMinutes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getDeliveryPartner() {
        return deliveryPartner;
    }

    public void setDeliveryPartner(User deliveryPartner) {
        this.deliveryPartner = deliveryPartner;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public void setEstimatedMinutes(Integer estimatedMinutes) {
        this.estimatedMinutes = estimatedMinutes;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", deliveryPartner=" + deliveryPartner.getFullName() +
                ", status=" + status +
                ", currentLocation='" + currentLocation + '\'' +
                ", estimatedMinutes=" + estimatedMinutes +
                '}';
    }
}

