package com.fooodie.services;

import com.fooodie.model.MenuItem;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartStore {

    public static class CartItem {
        public final MenuItem item;
        public int qty;
        public CartItem(MenuItem item, int qty) { this.item = item; this.qty = qty; }
    }

    // sessionToken -> (menuItemId -> CartItem)
    private static final Map<String, Map<Long, CartItem>> carts = new ConcurrentHashMap<>();

    public static Map<Long, CartItem> getCart(String token) {
        return carts.computeIfAbsent(token, k -> new LinkedHashMap<>());
    }

    public static void addItem(String token, MenuItem item, int qty) {
        Map<Long, CartItem> cart = getCart(token);
        cart.merge(item.getId(), new CartItem(item, qty),
                (existing, n) -> { existing.qty += qty; return existing; });
    }

    public static void removeItem(String token, long itemId) {
        getCart(token).remove(itemId);
    }

    public static void clearCart(String token) {
        carts.remove(token);
    }

    public static double total(String token) {
        return getCart(token).values().stream()
                .mapToDouble(ci -> ci.item.getPrice() * ci.qty).sum();
    }
}
