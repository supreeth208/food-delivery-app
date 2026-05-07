package com.fooodie.service;

import com.fooodie.model.Delivery;
import com.fooodie.repository.DeliveryRepository;
import java.util.Optional;

/**
 * DeliveryService for managing Delivery business logic
 */
public class DeliveryService {
    
    private DeliveryRepository deliveryRepository;

    public DeliveryService() {
        this.deliveryRepository = new DeliveryRepository();
    }

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Delivery createDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public Optional<Delivery> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }

    public Optional<Delivery> getDeliveryByOrderId(Long orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public Delivery updateDelivery(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    public void updateDeliveryStatus(Long deliveryId, Delivery.DeliveryStatus status) {
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        delivery.ifPresent(d -> {
            d.setStatus(status);
            deliveryRepository.save(d);
        });
    }

    public void updateDeliveryLocation(Long deliveryId, String currentLocation) {
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        delivery.ifPresent(d -> {
            d.setCurrentLocation(currentLocation);
            deliveryRepository.save(d);
        });
    }

    public void deleteDelivery(Long id) {
        deliveryRepository.delete(id);
    }
}

