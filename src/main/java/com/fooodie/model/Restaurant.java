package com.fooodie.model;

import java.util.List;

/**
 * Restaurant class representing restaurants in the Fooodie application
 */
public class Restaurant {
    
    private Long id;
    private String name;
    private String description;
    private String address;
    private String phoneNumber;
    private Double rating;
    private Double deliveryFee;
    private Integer estimatedDeliveryTime;
    private User owner;
    private List<MenuItem> menuItems;
    private Boolean isOpen;
    private Boolean active;

    // Constructors
    public Restaurant() {
        this.rating = 0.0;
        this.deliveryFee = 0.0;
        this.estimatedDeliveryTime = 30;
        this.isOpen = true;
        this.active = true;
    }

    public Restaurant(Long id, String name, String description, String address, 
                     String phoneNumber, Double rating, Double deliveryFee, 
                     Integer estimatedDeliveryTime, User owner, List<MenuItem> menuItems,
                     Boolean isOpen, Boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating != null ? rating : 0.0;
        this.deliveryFee = deliveryFee != null ? deliveryFee : 0.0;
        this.estimatedDeliveryTime = estimatedDeliveryTime != null ? estimatedDeliveryTime : 30;
        this.owner = owner;
        this.menuItems = menuItems;
        this.isOpen = isOpen != null ? isOpen : true;
        this.active = active != null ? active : true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public Integer getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Integer estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rating=" + rating +
                ", deliveryFee=" + deliveryFee +
                ", estimatedDeliveryTime=" + estimatedDeliveryTime +
                ", isOpen=" + isOpen +
                '}';
    }
}

