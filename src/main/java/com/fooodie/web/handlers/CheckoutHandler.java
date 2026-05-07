package com.fooodie.web.handlers;

import com.fooodie.db.OrderDao;
import com.fooodie.model.OrderItem;
import com.fooodie.services.CartStore;
import com.fooodie.services.CartStore.CartItem;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CheckoutHandler implements RouteHandler {

    private final OrderDao orderDao = new OrderDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String token = ctx.cookie("FOOODIE_SESSION");
        Map<Long, CartItem> cart = CartStore.getCart(token);
        if (cart.isEmpty()) return Response.redirect("/cart");

        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cart.values()) {
            items.add(new OrderItem(null, null, ci.item, ci.qty, ci.item.getPrice(), null));
        }

        long restaurantId = items.get(0).getMenuItem().getRestaurant().getId();
        double deliveryFee = items.get(0).getMenuItem().getRestaurant().getDeliveryFee();
        double subtotal = CartStore.total(token);

        orderDao.insertOrder(session.username, restaurantId, subtotal, deliveryFee, items);
        CartStore.clearCart(token);

        return Response.redirect("/orders");
    }
}
