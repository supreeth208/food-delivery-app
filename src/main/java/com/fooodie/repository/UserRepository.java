package com.fooodie.repository;

import com.fooodie.model.User;
import java.util.*;

/**
 * UserRepository for managing User data
 * This is an in-memory implementation for demonstration purposes
 */
public class UserRepository {
    
    private static final Map<Long, User> users = new HashMap<>();
    private static long idCounter = 1;

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        users.put(user.getId(), user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(Long id) {
        users.remove(id);
    }

    public void deleteAll() {
        users.clear();
    }

    public long count() {
        return users.size();
    }
}

