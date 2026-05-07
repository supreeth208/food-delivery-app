package com.fooodie.db;

import com.fooodie.model.MenuItem;
import com.fooodie.model.Order;
import com.fooodie.model.OrderItem;
import com.fooodie.model.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private final RestaurantDao restaurantDao = new RestaurantDao();

    /** Persist a new order and its items; returns the generated order id. */
    public long insertOrder(String username, long restaurantId,
                             double subtotal, double deliveryFee,
                             List<OrderItem> items) {
        String orderSql = "INSERT INTO orders(username,restaurant_id,status,subtotal,delivery_fee,total) VALUES(?,?,'CONFIRMED',?,?,?)";
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            long orderId;
            try (PreparedStatement ps = c.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, username);
                ps.setLong(2, restaurantId);
                ps.setDouble(3, subtotal);
                ps.setDouble(4, deliveryFee);
                ps.setDouble(5, subtotal + deliveryFee);
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) { k.next(); orderId = k.getLong(1); }
            }
            String itemSql = "INSERT INTO order_items(order_id,menu_item_id,name,price,quantity) VALUES(?,?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(itemSql)) {
                for (OrderItem oi : items) {
                    ps.setLong(1, orderId);
                    ps.setLong(2, oi.getMenuItem().getId());
                    ps.setString(3, oi.getMenuItem().getName());
                    ps.setDouble(4, oi.getPrice());
                    ps.setInt(5, oi.getQuantity());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            c.commit();
            return orderId;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    /** Load all orders for a user, newest first. */
    public List<Order> findByUsername(String username) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id,restaurant_id,status,subtotal,delivery_fee,total,created_at FROM orders WHERE username=? ORDER BY id DESC";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long oid = rs.getLong("id");
                    long rid = rs.getLong("restaurant_id");
                    Restaurant r = restaurantDao.findById(rid);
                    List<OrderItem> items = loadItems(c, oid, r);
                    double deliveryFee = rs.getDouble("delivery_fee");
                    double total = rs.getDouble("total");
                    Order.OrderStatus status = Order.OrderStatus.valueOf(rs.getString("status"));
                    Order o = new Order(oid, null, r, items, total, deliveryFee, status,
                            null, null, null, null, null);
                    orders.add(o);
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return orders;
    }

    /** Total order count across all users (for admin). */
    public int totalOrderCount() {
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM orders")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private List<OrderItem> loadItems(Connection c, long orderId, Restaurant r) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT id,menu_item_id,name,price,quantity FROM order_items WHERE order_id=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem m = new MenuItem();
                    m.setId(rs.getLong("menu_item_id"));
                    m.setName(rs.getString("name"));
                    m.setPrice(rs.getDouble("price"));
                    m.setRestaurant(r);
                    // restore image from live catalog if available
                    if (r != null) {
                        r.getMenuItems().stream()
                                .filter(mi -> mi.getId() == m.getId())
                                .findFirst()
                                .ifPresent(mi -> m.setImageUrl(mi.getImageUrl()));
                    }
                    OrderItem oi = new OrderItem(rs.getLong("id"), null, m,
                            rs.getInt("quantity"), rs.getDouble("price"), null);
                    items.add(oi);
                }
            }
        }
        return items;
    }
}
