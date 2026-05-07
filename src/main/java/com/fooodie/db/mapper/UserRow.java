package com.fooodie.db.mapper;

public record UserRow(long id, String username, String passwordHash, String role) {
}

