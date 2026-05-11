package com.fooodie.db;

import com.fooodie.model.MenuItem;
import com.fooodie.model.Restaurant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDao {

    /** Returns all restaurants with their menu items, seeding DB if empty. */
    public List<Restaurant> findAll() {
        ensureSeeded();
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT id,name,description,address,phone,rating,delivery_fee,eta_minutes,is_open,cover_url FROM restaurants ORDER BY id";
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Restaurant r = mapRestaurant(rs);
                r.setMenuItems(menuItemsFor(c, r));
                list.add(r);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public Restaurant findById(long id) {
        String sql = "SELECT id,name,description,address,phone,rating,delivery_fee,eta_minutes,is_open,cover_url FROM restaurants WHERE id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Restaurant r = mapRestaurant(rs);
                    r.setMenuItems(menuItemsFor(c, r));
                    return r;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    public MenuItem findMenuItemById(long itemId) {
        String sql = "SELECT mi.id,mi.name,mi.description,mi.price,mi.category,mi.image_url,mi.prep_time,mi.available,mi.restaurant_id FROM menu_items mi WHERE mi.id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MenuItem m = mapMenuItem(rs);
                    m.setRestaurant(findById(rs.getLong("restaurant_id")));
                    return m;
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return null;
    }

    // ── private helpers ──────────────────────────────────────────────────────

    private List<MenuItem> menuItemsFor(Connection c, Restaurant restaurant) throws SQLException {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id,name,description,price,category,image_url,prep_time,available FROM menu_items WHERE restaurant_id=? AND available=1 ORDER BY id";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, restaurant.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MenuItem m = mapMenuItem(rs);
                    m.setRestaurant(restaurant);  // always set restaurant back
                    items.add(m);
                }
            }
        }
        return items;
    }

    private Restaurant mapRestaurant(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setId(rs.getLong("id"));
        r.setName(rs.getString("name"));
        r.setDescription(rs.getString("description"));
        r.setAddress(rs.getString("address"));
        r.setPhoneNumber(rs.getString("phone"));
        r.setRating(rs.getDouble("rating"));
        r.setDeliveryFee(rs.getDouble("delivery_fee"));
        r.setEstimatedDeliveryTime(rs.getInt("eta_minutes"));
        r.setIsOpen(rs.getInt("is_open") == 1);
        r.setActive(true);
        return r;
    }

    private MenuItem mapMenuItem(ResultSet rs) throws SQLException {
        MenuItem m = new MenuItem();
        m.setId(rs.getLong("id"));
        m.setName(rs.getString("name"));
        m.setDescription(rs.getString("description"));
        m.setPrice(rs.getDouble("price"));
        m.setCategory(rs.getString("category"));
        m.setImageUrl(rs.getString("image_url"));
        m.setPreparationTime(rs.getInt("prep_time"));
        m.setAvailable(rs.getInt("available") == 1);
        return m;
    }

    // ── seeding ──────────────────────────────────────────────────────────────

    private static volatile boolean seeded = false;

    private synchronized void ensureSeeded() {
        if (seeded) return;
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM restaurants")) {
            if (rs.next() && rs.getInt(1) > 0) { seeded = true; return; }
        } catch (SQLException e) { throw new RuntimeException(e); }
        seedData();
        seeded = true;
    }

    private void seedData() {
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            long r1 = insertRestaurant(c, "Pizza Palace",    "Wood-fired artisan pizzas since 1998",       "456 Restaurant Ave", "555-PIZZA", 4.6, 2.99, 30, "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80");
            long r2 = insertRestaurant(c, "Sushi World",     "Premium omakase & rolls, flown-in daily",    "12 Ocean Street",    "555-SUSHI", 4.8, 3.49, 40, "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&q=80");
            long r3 = insertRestaurant(c, "Burger Barn",     "Smash burgers & loaded fries, no shortcuts", "88 Grill Street",    "555-BURG",  4.5, 1.99, 25, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80");
            long r4 = insertRestaurant(c, "Spice Garden",    "Authentic Indian curries & tandoor",         "22 Curry Lane",      "555-SPCE",  4.7, 2.49, 35, "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&q=80");
            long r5 = insertRestaurant(c, "Taco Fiesta",     "Street-style Mexican tacos & burritos",      "7 Salsa Road",       "555-TACO",  4.4, 1.49, 20, "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=800&q=80");
            long r6 = insertRestaurant(c, "The Pasta House", "Handmade pasta & Italian classics",          "33 Noodle Blvd",     "555-PAST",  4.6, 2.99, 35, "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80");

            // Pizza Palace
            insertItem(c, r1, "Margherita",       "San Marzano tomato, fresh mozzarella, basil",         12.99, "Pizza",   15, "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=600&q=80");
            insertItem(c, r1, "Pepperoni Feast",  "Double pepperoni, mozzarella, oregano",               14.99, "Pizza",   15, "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=600&q=80");
            insertItem(c, r1, "BBQ Chicken",      "Smoky BBQ sauce, grilled chicken, red onion",         15.49, "Pizza",   18, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600&q=80");
            insertItem(c, r1, "Truffle Mushroom", "Wild mushrooms, truffle oil, parmesan, rocket",       16.99, "Pizza",   20, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80");
            // Sushi World
            insertItem(c, r2, "California Roll",  "Crab, avocado, cucumber, sesame",                    13.50, "Rolls",   20, "https://images.unsplash.com/photo-1617196034183-421b4040ed20?w=600&q=80");
            insertItem(c, r2, "Salmon Nigiri",    "Hand-pressed rice, fresh Atlantic salmon",            15.25, "Nigiri",  18, "https://images.unsplash.com/photo-1553621042-f6e147245754?w=600&q=80");
            insertItem(c, r2, "Dragon Roll",      "Shrimp tempura, avocado top, eel sauce",              17.99, "Rolls",   25, "https://images.unsplash.com/photo-1559410545-0bdcd187e0a6?w=600&q=80");
            insertItem(c, r2, "Miso Ramen",       "Rich pork broth, soft egg, nori, bamboo shoots",     14.50, "Ramen",   22, "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600&q=80");
            // Burger Barn
            insertItem(c, r3, "Classic Smash",    "Double smash patty, American cheese, pickles",       11.99, "Burgers", 12, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80");
            insertItem(c, r3, "Bacon Blaze",      "Crispy bacon, cheddar, jalapenos, chipotle mayo",    13.49, "Burgers", 14, "https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=600&q=80");
            insertItem(c, r3, "Truffle Fries",    "Hand-cut fries, truffle oil, parmesan, herbs",        6.99, "Sides",    8, "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=600&q=80");
            insertItem(c, r3, "Oreo Milkshake",   "Thick vanilla shake blended with Oreo cookies",       5.99, "Drinks",   5, "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=600&q=80");
            // Spice Garden
            insertItem(c, r4, "Butter Chicken",   "Tender chicken in rich tomato-cream masala",         15.99, "Curry",   25, "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=600&q=80");
            insertItem(c, r4, "Paneer Tikka",     "Marinated cottage cheese, bell peppers, tandoor",    13.99, "Starters", 20, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600&q=80");
            insertItem(c, r4, "Garlic Naan",      "Soft leavened bread with garlic butter",              3.49, "Bread",   10, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600&q=80");
            insertItem(c, r4, "Mango Lassi",      "Chilled yogurt drink with Alphonso mango",            4.49, "Drinks",   5, "https://images.unsplash.com/photo-1527661591475-527312dd65f5?w=600&q=80");
            // Taco Fiesta
            insertItem(c, r5, "Carne Asada Taco", "Grilled beef, pico de gallo, guac, cotija",          10.99, "Tacos",   15, "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=600&q=80");
            insertItem(c, r5, "Chicken Burrito",  "Seasoned chicken, rice, beans, sour cream, salsa",   12.49, "Burritos", 18, "https://images.unsplash.com/photo-1626700051175-6818013e1d4f?w=600&q=80");
            insertItem(c, r5, "Loaded Nachos",    "Tortilla chips, cheese, jalapenos, guacamole",        9.99, "Sides",   10, "https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=600&q=80");
            insertItem(c, r5, "Horchata",         "Sweet cinnamon rice milk, served chilled",            3.99, "Drinks",   3, "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=600&q=80");
            // The Pasta House
            insertItem(c, r6, "Spaghetti Carbonara", "Guanciale, egg yolk, pecorino, black pepper",     14.99, "Pasta",   20, "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=600&q=80");
            insertItem(c, r6, "Penne Arrabbiata",    "Spicy tomato, garlic, chilli, fresh basil",       12.99, "Pasta",   18, "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=600&q=80");
            insertItem(c, r6, "Tiramisu",            "Espresso-soaked ladyfingers, mascarpone cream",    7.99, "Dessert", 10, "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=600&q=80");
            insertItem(c, r6, "Bruschetta",          "Toasted ciabatta, tomato, basil, extra virgin oil", 6.49, "Starters", 8, "https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?w=600&q=80");

            // Seed demo users (admin123, customer123 SHA-256)
            seedUser(c, "admin",    "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9", "ADMIN",    "Admin User",     "admin@fooodie.com");
            seedUser(c, "customer", "b041c0aeb35bb0fa4aa668ca5a920b590196fdaf9a00eb852c9b7f4d123cc6d6", "CUSTOMER", "Demo Customer",  "customer@fooodie.com");

            c.commit();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private long insertRestaurant(Connection c, String name, String desc, String addr,
                                   String phone, double rating, double fee, int eta, String cover) throws SQLException {
        String sql = "INSERT INTO restaurants(name,description,address,phone,rating,delivery_fee,eta_minutes,cover_url) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name); ps.setString(2, desc); ps.setString(3, addr);
            ps.setString(4, phone); ps.setDouble(5, rating); ps.setDouble(6, fee);
            ps.setInt(7, eta); ps.setString(8, cover);
            ps.executeUpdate();
            try (ResultSet k = ps.getGeneratedKeys()) { k.next(); return k.getLong(1); }
        }
    }

    private void insertItem(Connection c, long rid, String name, String desc,
                             double price, String cat, int prep, String img) throws SQLException {
        String sql = "INSERT INTO menu_items(restaurant_id,name,description,price,category,prep_time,image_url) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, rid); ps.setString(2, name); ps.setString(3, desc);
            ps.setDouble(4, price); ps.setString(5, cat); ps.setInt(6, prep); ps.setString(7, img);
            ps.executeUpdate();
        }
    }

    private void seedUser(Connection c, String username, String hash, String role,
                           String fullName, String email) throws SQLException {
        String check = "SELECT 1 FROM users WHERE username=?";
        try (PreparedStatement ps = c.prepareStatement(check)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return; }
        }
        String sql = "INSERT INTO users(username,password_hash,role,full_name,email) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username); ps.setString(2, hash); ps.setString(3, role);
            ps.setString(4, fullName); ps.setString(5, email);
            ps.executeUpdate();
        }
    }
}
