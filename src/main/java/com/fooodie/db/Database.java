package com.fooodie.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:fooodie.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initSchema();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialise SQLite", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection c = DriverManager.getConnection(DB_URL);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA journal_mode=WAL");
            st.execute("PRAGMA foreign_keys=ON");
        }
        return c;
    }

    private static void initSchema() throws SQLException {
        try (Connection c = DriverManager.getConnection(DB_URL);
             Statement st = c.createStatement()) {

            st.execute("PRAGMA foreign_keys=ON");

            // users
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    username      TEXT    NOT NULL UNIQUE,
                    password_hash TEXT    NOT NULL,
                    role          TEXT    NOT NULL DEFAULT 'CUSTOMER',
                    full_name     TEXT,
                    email         TEXT,
                    active        INTEGER NOT NULL DEFAULT 1
                )""");

            // restaurants
            st.execute("""
                CREATE TABLE IF NOT EXISTS restaurants (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    name          TEXT    NOT NULL,
                    description   TEXT,
                    address       TEXT,
                    phone         TEXT,
                    rating        REAL    DEFAULT 0,
                    delivery_fee  REAL    DEFAULT 0,
                    eta_minutes   INTEGER DEFAULT 30,
                    is_open       INTEGER DEFAULT 1,
                    cover_url     TEXT
                )""");

            // menu_items
            st.execute("""
                CREATE TABLE IF NOT EXISTS menu_items (
                    id               INTEGER PRIMARY KEY AUTOINCREMENT,
                    restaurant_id    INTEGER NOT NULL REFERENCES restaurants(id),
                    name             TEXT    NOT NULL,
                    description      TEXT,
                    price            REAL    NOT NULL,
                    category         TEXT,
                    image_url        TEXT,
                    prep_time        INTEGER DEFAULT 0,
                    available        INTEGER DEFAULT 1
                )""");

            // orders
            st.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id            INTEGER PRIMARY KEY AUTOINCREMENT,
                    username      TEXT    NOT NULL,
                    restaurant_id INTEGER NOT NULL REFERENCES restaurants(id),
                    status        TEXT    NOT NULL DEFAULT 'CONFIRMED',
                    subtotal      REAL    NOT NULL,
                    delivery_fee  REAL    NOT NULL,
                    total         REAL    NOT NULL,
                    created_at    TEXT    NOT NULL DEFAULT (datetime('now'))
                )""");

            // order_items
            st.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id           INTEGER PRIMARY KEY AUTOINCREMENT,
                    order_id     INTEGER NOT NULL REFERENCES orders(id),
                    menu_item_id INTEGER NOT NULL REFERENCES menu_items(id),
                    name         TEXT    NOT NULL,
                    price        REAL    NOT NULL,
                    quantity     INTEGER NOT NULL
                )""");
        }
    }
}
