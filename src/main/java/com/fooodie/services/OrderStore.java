package com.fooodie.services;

import com.fooodie.model.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderStore {

    private static final Map<String, List<Order>> orders = new ConcurrentHashMap<>();
    private static final AtomicLong idSeq = new AtomicLong(1);

    public static long nextId() { return idSeq.getAndIncrement(); }

    public static void save(String username, Order order) {
        orders.computeIfAbsent(username, k -> new ArrayList<>()).add(0, order);
    }

    public static List<Order> getOrders(String username) {
        return orders.getOrDefault(username, Collections.emptyList());
    }
}
