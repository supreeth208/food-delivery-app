package com.fooodie.web.handlers;

import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;

public class LogoutHandler implements RouteHandler {
    @Override
    public Response handle(RequestContext ctx) {
        SessionManager.clearSession(ctx);
        Response r = Response.redirect("/login");
        r.headers.put("Set-Cookie", "FOOODIE_SESSION=; Path=/; Max-Age=0; HttpOnly");
        return r;
    }
}

