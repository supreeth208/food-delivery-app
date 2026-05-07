package com.fooodie.web.handlers;

import com.fooodie.db.RestaurantDao;
import com.fooodie.model.MenuItem;
import com.fooodie.model.Restaurant;
import com.fooodie.services.CartStore;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;

public class CartAddHandler implements RouteHandler {

    private final RestaurantDao dao = new RestaurantDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String itemIdStr = ctx.formParam("itemId");
        String restaurantIdStr = ctx.formParam("restaurantId");
        if (itemIdStr == null || restaurantIdStr == null) return Response.redirect("/restaurants");

        long restaurantId = Long.parseLong(restaurantIdStr);
        long itemId = Long.parseLong(itemIdStr);

        Restaurant r = dao.findById(restaurantId);
        if (r == null) return Response.redirect("/restaurants");

        MenuItem found = r.getMenuItems().stream()
                .filter(m -> m.getId() == itemId).findFirst().orElse(null);
        if (found == null) return Response.redirect("/restaurants");

        CartStore.addItem(ctx.cookie("FOOODIE_SESSION"), found, 1);
        return Response.redirect("/cart");
    }
}
