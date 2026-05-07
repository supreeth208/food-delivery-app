package com.fooodie.web.handlers;

import com.fooodie.db.AuthDao;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class RegisterHandler implements RouteHandler {

    private final AuthDao authDao = new AuthDao();

    @Override
    public Response handle(RequestContext ctx) {
        String fullName = ctx.formParam("fullName");
        String email = ctx.formParam("email");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        if (fullName == null || email == null || username == null || password == null) {
            return Response.redirect("/register");
        }

        if (authDao.usernameExists(username)) {
            return Response.html(200, "<h3>Username already exists</h3><a href='/register'>Back</a>");
        }

        if (password.length() < 6) {
            return Response.html(200, "<h3>Password must be at least 6 characters</h3><a href='/register'>Back</a>");
        }

        String hash = sha256Hex(password);
        authDao.insertUser(username, hash, "CUSTOMER", fullName, email);

        // Auto-login after register
        var rowOpt = authDao.findByUsername(username);
        if (rowOpt.isPresent()) {
            var u = rowOpt.get();
            return com.fooodie.web.session.SessionManager.createSession(ctx, u.id(), u.role(), u.username());
        }

        return Response.redirect("/login");
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

