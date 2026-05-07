package com.fooodie.web.handlers;

import com.fooodie.services.CartStore;
import com.fooodie.services.OrderStore;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

public class ProfileHandler implements RouteHandler {

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String token = ctx.cookie("FOOODIE_SESSION");
        int cartCount = token != null ? CartStore.getCart(token).size() : 0;
        int orderCount = OrderStore.getOrders(session.username).size();
        String initial = session.username.substring(0, 1).toUpperCase();

        String body =
            "<div class=\"section-title\">My Profile</div>"
          + "<div class=\"row g-4\">"

          // Avatar + info card
          + "<div class=\"col-md-4\">"
          + "<div class=\"card\"><div class=\"card-body p-4 text-center\">"
          + "<div style=\"width:80px;height:80px;border-radius:50%;background:var(--brand);color:#fff;font-size:2rem;font-weight:800;display:flex;align-items:center;justify-content:center;margin:0 auto 16px\">" + esc(initial) + "</div>"
          + "<h5 class=\"fw-bold mb-0\">" + esc(session.username) + "</h5>"
          + "<span class=\"cat-pill mt-2 d-inline-block\">" + esc(session.role) + "</span>"
          + "<hr/>"
          + "<a href=\"/orders\" class=\"btn btn-brand w-100 mb-2\">My Orders</a>"
          + "<a href=\"/logout\" class=\"btn btn-outline-brand w-100\">Logout</a>"
          + "</div></div></div>"

          // Stats
          + "<div class=\"col-md-8\">"
          + "<div class=\"row g-3\">"
          + stat("Total Orders", String.valueOf(orderCount), "#ff6b35")
          + stat("Cart Items", String.valueOf(cartCount), "#8b5cf6")
          + stat("Member Since", "2025", "#10b981")
          + "</div>"

          + "<div class=\"card mt-3\"><div class=\"card-body p-4\">"
          + "<h6 class=\"fw-bold mb-3\">Account Details</h6>"
          + "<table class=\"table mb-0\" style=\"font-size:.9rem\">"
          + "<tr><td class=\"text-muted\" style=\"width:140px\">Username</td><td class=\"fw-500\">" + esc(session.username) + "</td></tr>"
          + "<tr><td class=\"text-muted\">Role</td><td class=\"fw-500\">" + esc(session.role) + "</td></tr>"
          + "<tr><td class=\"text-muted\">User ID</td><td class=\"fw-500\">" + session.userId + "</td></tr>"
          + "</table>"
          + "</div></div>"
          + "</div>"

          + "</div>";

        return Response.html(200, HtmlPage.layout("Profile", body, "/profile",
                session.username, cartCount));
    }

    private static String stat(String label, String value, String color) {
        return "<div class=\"col-sm-4\">"
             + "<div class=\"card\"><div class=\"card-body p-3 text-center\">"
             + "<div style=\"font-size:1.8rem;font-weight:800;color:" + color + "\">" + value + "</div>"
             + "<div class=\"text-muted\" style=\"font-size:.82rem\">" + label + "</div>"
             + "</div></div></div>";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
