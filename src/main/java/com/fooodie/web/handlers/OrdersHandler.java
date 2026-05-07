package com.fooodie.web.handlers;

import com.fooodie.db.OrderDao;
import com.fooodie.model.Order;
import com.fooodie.model.OrderItem;
import com.fooodie.services.CartStore;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

import java.util.List;

public class OrdersHandler implements RouteHandler {

    private final OrderDao orderDao = new OrderDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String token = ctx.cookie("FOOODIE_SESSION");
        int cartCount = token != null ? CartStore.getCart(token).size() : 0;
        List<Order> orders = orderDao.findByUsername(session.username);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"section-title\">My Orders</div>");

        if (orders.isEmpty()) {
            sb.append("<div class=\"card\"><div class=\"card-body text-center py-5\">")
              .append("<h5 class=\"fw-bold\">No orders yet</h5>")
              .append("<p class=\"text-muted\">Your order history will appear here.</p>")
              .append("<a href=\"/restaurants\" class=\"btn btn-brand px-4\">Order Now</a>")
              .append("</div></div>");
        } else {
            for (Order o : orders) {
                String color = statusColor(o.getStatus());
                sb.append("<div class=\"card mb-3\"><div class=\"card-body p-4\">")
                  .append("<div class=\"d-flex justify-content-between align-items-start mb-3\">")
                  .append("<div><h6 class=\"fw-bold mb-0\">Order #").append(o.getId()).append("</h6>")
                  .append("<span class=\"text-muted\" style=\"font-size:.82rem\">").append(esc(o.getRestaurant().getName())).append("</span></div>")
                  .append("<span class=\"status-badge\" style=\"background:").append(color).append("22;color:").append(color).append("\">").append(o.getStatus()).append("</span>")
                  .append("</div><div class=\"row g-2 mb-3\">");

                for (OrderItem oi : o.getOrderItems()) {
                    String img = oi.getMenuItem().getImageUrl();
                    sb.append("<div class=\"col-12\"><div class=\"d-flex align-items-center gap-3\">");
                    if (img != null) sb.append("<img src=\"").append(img).append("\" style=\"width:44px;height:44px;border-radius:8px;object-fit:cover\"/>");
                    sb.append("<div class=\"flex-grow-1\"><div class=\"fw-500\" style=\"font-size:.9rem\">").append(esc(oi.getMenuItem().getName())).append("</div>")
                      .append("<small class=\"text-muted\">x").append(oi.getQuantity()).append("</small></div>")
                      .append("<div class=\"fw-600\">$").append(String.format("%.2f", oi.getSubtotal())).append("</div>")
                      .append("</div></div>");
                }

                sb.append("</div><div class=\"d-flex justify-content-between align-items-center pt-2\" style=\"border-top:1px solid #f0f0f0\">")
                  .append("<span class=\"text-muted\" style=\"font-size:.82rem\">Delivery: $").append(String.format("%.2f", o.getDeliveryFee())).append("</span>")
                  .append("<span class=\"fw-bold\" style=\"color:var(--brand);font-size:1rem\">Total: $").append(String.format("%.2f", o.getTotalAmount())).append("</span>")
                  .append("</div></div></div>");
            }
        }

        return Response.html(200, HtmlPage.layout("Orders", sb.toString(), "/orders",
                session.username, cartCount));
    }

    private static String statusColor(Order.OrderStatus s) {
        switch (s) {
            case CONFIRMED: return "#10b981";
            case PREPARING: return "#f59e0b";
            case OUT_FOR_DELIVERY: return "#3b82f6";
            case DELIVERED: return "#6b7280";
            case CANCELLED: return "#ef4444";
            default: return "#ff6b35";
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
