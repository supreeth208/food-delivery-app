package com.fooodie.web.handlers;

import com.fooodie.services.CartStore;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;

public class CartRemoveHandler implements RouteHandler {

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String itemIdStr = ctx.formParam("itemId");
        if (itemIdStr != null) {
            CartStore.removeItem(ctx.cookie("FOOODIE_SESSION"), Long.parseLong(itemIdStr));
        }
        return Response.redirect("/cart");
    }
}
