package com.fooodie.web.handlers;

import com.fooodie.db.AuthDao;
import com.fooodie.db.mapper.UserRow;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class LoginHandler implements RouteHandler {

    private final AuthDao authDao = new AuthDao();

    @Override
    public Response handle(RequestContext ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (username == null || password == null) {
            return Response.redirect("/login");
        }

        var uOpt = authDao.findByUsername(username);
        if (uOpt.isEmpty()) {
            return Response.html(200, "<h3>Invalid credentials</h3><a href='/login'>Back</a>");
        }

        UserRow u = uOpt.get();
        String hashed = sha256Hex(password);
        if (!hashed.equals(u.passwordHash())) {
            return Response.html(200, "<h3>Invalid credentials</h3><a href='/login'>Back</a>");
        }

        return SessionManager.createSession(ctx, u.id(), u.role(), u.username());
    }

    private String sha256Hex(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

