package com.fooodie.services;

import com.fooodie.model.MenuItem;
import com.fooodie.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCatalog {

    private static final List<Restaurant> restaurants = new ArrayList<>();

    public static synchronized List<Restaurant> allRestaurants() {
        if (restaurants.isEmpty()) seed();
        return restaurants;
    }

    public static synchronized Restaurant restaurantById(long id) {
        return allRestaurants().stream()
                .filter(r -> r.getId() != null && r.getId() == id)
                .findFirst().orElse(null);
    }

    private static Restaurant mkR(long id, String name, String desc, String addr,
                                   String phone, double rating, double fee, int eta) {
        Restaurant r = new Restaurant();
        r.setId(id); r.setName(name); r.setDescription(desc);
        r.setAddress(addr); r.setPhoneNumber(phone);
        r.setRating(rating); r.setDeliveryFee(fee);
        r.setEstimatedDeliveryTime(eta);
        r.setIsOpen(true); r.setActive(true);
        r.setMenuItems(new ArrayList<>());
        return r;
    }

    private static MenuItem mkM(long id, Restaurant r, String name, String desc,
                                 double price, String cat, int prepTime, String img) {
        MenuItem m = new MenuItem();
        m.setId(id); m.setName(name); m.setDescription(desc);
        m.setPrice(price); m.setCategory(cat);
        m.setAvailable(true); m.setPreparationTime(prepTime);
        m.setRestaurant(r); m.setImageUrl(img);
        return m;
    }

    private static void seed() {

        // 1 — Pizza Palace
        Restaurant r1 = mkR(1, "Pizza Palace", "Wood-fired artisan pizzas since 1998",
                "456 Restaurant Ave", "555-PIZZA", 4.6, 2.99, 30);
        r1.getMenuItems().add(mkM(1,  r1, "Margherita",       "San Marzano tomato, fresh mozzarella, basil",          12.99, "Pizza",  15, "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=600&q=80"));
        r1.getMenuItems().add(mkM(2,  r1, "Pepperoni Feast",  "Double pepperoni, mozzarella, oregano",                14.99, "Pizza",  15, "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=600&q=80"));
        r1.getMenuItems().add(mkM(3,  r1, "BBQ Chicken",      "Smoky BBQ sauce, grilled chicken, red onion",          15.49, "Pizza",  18, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600&q=80"));
        r1.getMenuItems().add(mkM(4,  r1, "Truffle Mushroom", "Wild mushrooms, truffle oil, parmesan, rocket",        16.99, "Pizza",  20, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80"));

        // 2 — Sushi World
        Restaurant r2 = mkR(2, "Sushi World", "Premium omakase & rolls, flown-in daily",
                "12 Ocean Street", "555-SUSHI", 4.8, 3.49, 40);
        r2.getMenuItems().add(mkM(5,  r2, "California Roll",  "Crab, avocado, cucumber, sesame",                      13.50, "Rolls",  20, "https://images.unsplash.com/photo-1617196034183-421b4040ed20?w=600&q=80"));
        r2.getMenuItems().add(mkM(6,  r2, "Salmon Nigiri",    "Hand-pressed rice, fresh Atlantic salmon",             15.25, "Nigiri", 18, "https://images.unsplash.com/photo-1553621042-f6e147245754?w=600&q=80"));
        r2.getMenuItems().add(mkM(7,  r2, "Dragon Roll",      "Shrimp tempura, avocado top, eel sauce",               17.99, "Rolls",  25, "https://images.unsplash.com/photo-1559410545-0bdcd187e0a6?w=600&q=80"));
        r2.getMenuItems().add(mkM(8,  r2, "Miso Ramen",       "Rich pork broth, soft egg, nori, bamboo shoots",       14.50, "Ramen",  22, "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600&q=80"));

        // 3 — Burger Barn
        Restaurant r3 = mkR(3, "Burger Barn", "Smash burgers & loaded fries, no shortcuts",
                "88 Grill Street", "555-BURG", 4.5, 1.99, 25);
        r3.getMenuItems().add(mkM(9,  r3, "Classic Smash",    "Double smash patty, American cheese, pickles, sauce",  11.99, "Burgers", 12, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80"));
        r3.getMenuItems().add(mkM(10, r3, "Bacon Blaze",      "Crispy bacon, cheddar, jalapenos, chipotle mayo",      13.49, "Burgers", 14, "https://images.unsplash.com/photo-1553979459-d2229ba7433b?w=600&q=80"));
        r3.getMenuItems().add(mkM(11, r3, "Truffle Fries",    "Hand-cut fries, truffle oil, parmesan, herbs",          6.99, "Sides",   8,  "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=600&q=80"));
        r3.getMenuItems().add(mkM(12, r3, "Oreo Milkshake",   "Thick vanilla shake blended with Oreo cookies",         5.99, "Drinks",  5,  "https://images.unsplash.com/photo-1572490122747-3968b75cc699?w=600&q=80"));

        // 4 — Spice Garden
        Restaurant r4 = mkR(4, "Spice Garden", "Authentic Indian curries & tandoor specialties",
                "22 Curry Lane", "555-SPCE", 4.7, 2.49, 35);
        r4.getMenuItems().add(mkM(13, r4, "Butter Chicken",   "Tender chicken in rich tomato-cream masala",           15.99, "Curry",   25, "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=600&q=80"));
        r4.getMenuItems().add(mkM(14, r4, "Paneer Tikka",     "Marinated cottage cheese, bell peppers, tandoor",      13.99, "Starters",20, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600&q=80"));
        r4.getMenuItems().add(mkM(15, r4, "Garlic Naan",      "Soft leavened bread with garlic butter",                3.49, "Bread",   10, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600&q=80"));
        r4.getMenuItems().add(mkM(16, r4, "Mango Lassi",      "Chilled yogurt drink with Alphonso mango",              4.49, "Drinks",   5, "https://images.unsplash.com/photo-1527661591475-527312dd65f5?w=600&q=80"));

        // 5 — Taco Fiesta
        Restaurant r5 = mkR(5, "Taco Fiesta", "Street-style Mexican tacos & burritos",
                "7 Salsa Road", "555-TACO", 4.4, 1.49, 20);
        r5.getMenuItems().add(mkM(17, r5, "Carne Asada Taco", "Grilled beef, pico de gallo, guac, cotija",            10.99, "Tacos",   15, "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=600&q=80"));
        r5.getMenuItems().add(mkM(18, r5, "Chicken Burrito",  "Seasoned chicken, rice, beans, sour cream, salsa",     12.49, "Burritos", 18, "https://images.unsplash.com/photo-1626700051175-6818013e1d4f?w=600&q=80"));
        r5.getMenuItems().add(mkM(19, r5, "Loaded Nachos",    "Tortilla chips, cheese, jalapenos, guacamole",          9.99, "Sides",   10, "https://images.unsplash.com/photo-1513456852971-30c0b8199d4d?w=600&q=80"));
        r5.getMenuItems().add(mkM(20, r5, "Horchata",         "Sweet cinnamon rice milk, served chilled",              3.99, "Drinks",   3, "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=600&q=80"));

        // 6 — The Pasta House
        Restaurant r6 = mkR(6, "The Pasta House", "Handmade pasta & Italian classics",
                "33 Noodle Blvd", "555-PAST", 4.6, 2.99, 35);
        r6.getMenuItems().add(mkM(21, r6, "Spaghetti Carbonara", "Guanciale, egg yolk, pecorino, black pepper",       14.99, "Pasta",   20, "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=600&q=80"));
        r6.getMenuItems().add(mkM(22, r6, "Penne Arrabbiata",    "Spicy tomato, garlic, chilli, fresh basil",         12.99, "Pasta",   18, "https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?w=600&q=80"));
        r6.getMenuItems().add(mkM(23, r6, "Tiramisu",            "Espresso-soaked ladyfingers, mascarpone cream",      7.99, "Dessert", 10, "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=600&q=80"));
        r6.getMenuItems().add(mkM(24, r6, "Bruschetta",          "Toasted ciabatta, tomato, basil, extra virgin oil",  6.49, "Starters", 8, "https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?w=600&q=80"));

        restaurants.add(r1); restaurants.add(r2); restaurants.add(r3);
        restaurants.add(r4); restaurants.add(r5); restaurants.add(r6);
    }
}
