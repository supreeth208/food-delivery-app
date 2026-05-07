package com.fooodie.db;

import com.fooodie.db.mapper.UserRow;

import java.sql.*;
import java.util.Optional;

public class AuthDao {

    public Optional<UserRow> findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, role FROM users WHERE username=? AND active=1";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new UserRow(
                            rs.getLong("id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role")));
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return Optional.empty();
    }

    public void insertUser(String username, String passwordHash, String role,
                           String fullName, String email) {
        String sql = "INSERT INTO users(username,password_hash,role,full_name,email) VALUES(?,?,?,?,?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, role);
            ps.setString(4, fullName);
            ps.setString(5, email);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username=? LIMIT 1";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
