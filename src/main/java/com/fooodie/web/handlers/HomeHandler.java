package com.fooodie.web.handlers;

import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

public class HomeHandler implements RouteHandler {
    @Override
    public Response handle(RequestContext ctx) {
        UserSession s = SessionManager.getSession(ctx);
        // Root always goes to login if not authenticated
        return Response.redirect(s != null ? "/home" : "/login");
    }
}
