package com.fooodie.web.session;

import com.fooodie.db.AuthDao;
import com.fooodie.db.Database;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory session store.
 * (Beginner-friendly; persists only while server is running.)
 */
public class SessionManager {

    private static final String COOKIE_NAME = "FOOODIE_SESSION";
    private static final Map<String, UserSession> sessions = new ConcurrentHashMap<>();

    public static UserSession getSession(RequestContext ctx) {
        String token = ctx.cookie(COOKIE_NAME);
        if (token == null) return null;
        return sessions.get(token);
    }

    public static Response createSession(RequestContext ctx, long userId, String role, String username) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new UserSession(userId, role, username));
        Response r = Response.redirect("/home");
        r.headers.put("Set-Cookie", COOKIE_NAME + "=" + token + "; Path=/; HttpOnly");
        return r;
    }

    public static void clearSession(RequestContext ctx) {
        String token = ctx.cookie(COOKIE_NAME);
        if (token != null) sessions.remove(token);
    }
}

