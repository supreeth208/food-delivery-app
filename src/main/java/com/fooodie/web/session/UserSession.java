package com.fooodie.web.session;

public class UserSession {
    public final long userId;
    public final String role;
    public final String username;

    public UserSession(long userId, String role, String username) {
        this.userId = userId;
        this.role = role;
        this.username = username;
    }
}

