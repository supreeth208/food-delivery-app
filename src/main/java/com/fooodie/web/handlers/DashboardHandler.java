package com.fooodie.web.handlers;

import com.fooodie.db.OrderDao;
import com.fooodie.services.CartStore;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

public class DashboardHandler implements RouteHandler {

    private final OrderDao orderDao = new OrderDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession s = SessionManager.getSession(ctx);
        if (s == null) return Response.redirect("/login");

        String token = ctx.cookie("FOOODIE_SESSION");
        int cartCount = token != null ? CartStore.getCart(token).size() : 0;
        int orderCount = orderDao.findByUsername(s.username).size();

        String body =
            // Hero
            "<div class=\"hero mb-5\">"
          + "  <div class=\"row align-items-center\">"
          + "    <div class=\"col-lg-7\" style=\"position:relative;z-index:1\">"
          + "      <div class=\"cat-pill mb-3\">Welcome back, " + esc(s.username) + "!</div>"
          + "      <h1>Hungry? <span style=\"color:var(--brand)\">Order now</span></h1>"
          + "      <p class=\"mt-3\">Browse restaurants, add to cart, and get your meal delivered fast.</p>"
          + "      <div class=\"d-flex gap-3 mt-4\">"
          + "        <a href=\"/restaurants\" class=\"btn btn-brand px-4 py-2\">Browse Restaurants</a>"
          + "        <a href=\"/orders\" class=\"btn btn-outline-light px-4 py-2\">My Orders</a>"
          + "      </div>"
          + "    </div>"
          + "    <div class=\"col-lg-5 d-none d-lg-block text-end\" style=\"position:relative;z-index:1\">"
          + "      <img src=\"https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=500&q=80\""
          + "           style=\"width:100%;border-radius:16px;box-shadow:0 20px 60px rgba(0,0,0,.4)\" alt=\"food\"/>"
          + "    </div>"
          + "  </div>"
          + "</div>"

          // Quick stats
          + "<div class=\"row g-3 mb-5\">"
          + quickStat("Cart Items",   String.valueOf(cartCount),  "#ff6b35", "/cart",    "View Cart")
          + quickStat("Past Orders",  String.valueOf(orderCount), "#8b5cf6", "/orders",  "View Orders")
          + quickStat("Restaurants",  "6",                        "#10b981", "/restaurants", "Browse")
          + "</div>"

          // Features
          + "<div class=\"section-title\">Why Fooodie?</div>"
          + "<div class=\"row g-4 mb-5\">"
          + feature("Fast Delivery",    "Most orders arrive in under 40 minutes.", "#ff6b35", "M13 10V3L4 14h7v7l9-11h-7z")
          + feature("Top Restaurants",  "Curated selection of the best local spots.", "#8b5cf6", "M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6")
          + feature("Easy Ordering",    "Add items, checkout in seconds.", "#10b981", "M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2")
          + "</div>"

          // Cuisines
          + "<div class=\"section-title\">Popular Cuisines</div>"
          + "<div class=\"row g-3\">"
          + cuisine("Pizza",   "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400&q=80")
          + cuisine("Sushi",   "https://images.unsplash.com/photo-1617196034183-421b4040ed20?w=400&q=80")
          + cuisine("Burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80")
          + cuisine("Indian",  "https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?w=400&q=80")
          + cuisine("Mexican", "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=400&q=80")
          + cuisine("Pasta",   "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=400&q=80")
          + "</div>";

        return Response.html(200, HtmlPage.layout("Home", body, "/home", s.username, cartCount));
    }

    private static String quickStat(String label, String value, String color, String href, String btnText) {
        return "<div class=\"col-md-4\">"
             + "<div class=\"card\"><div class=\"card-body p-4 d-flex align-items-center gap-3\">"
             + "<div style=\"width:56px;height:56px;border-radius:14px;background:" + color + "22;display:flex;align-items:center;justify-content:center;flex-shrink:0\">"
             + "<span style=\"font-size:1.5rem;font-weight:800;color:" + color + "\">" + value + "</span>"
             + "</div>"
             + "<div><div class=\"fw-600\">" + label + "</div>"
             + "<a href=\"" + href + "\" style=\"font-size:.82rem;color:" + color + ";font-weight:600\">" + btnText + " &rarr;</a>"
             + "</div></div></div></div>";
    }

    private static String feature(String title, String desc, String color, String path) {
        return "<div class=\"col-md-4\"><div class=\"card h-100\"><div class=\"card-body p-4\">"
             + "<div style=\"width:44px;height:44px;border-radius:12px;background:" + color + "22;display:flex;align-items:center;justify-content:center;margin-bottom:12px\">"
             + "<svg width=\"22\" height=\"22\" fill=\"none\" stroke=\"" + color + "\" stroke-width=\"2\" viewBox=\"0 0 24 24\"><path d=\"" + path + "\"/></svg>"
             + "</div>"
             + "<h6 class=\"fw-bold mb-1\">" + title + "</h6>"
             + "<p class=\"text-muted mb-0\" style=\"font-size:.88rem\">" + desc + "</p>"
             + "</div></div></div>";
    }

    private static String cuisine(String name, String img) {
        return "<div class=\"col-6 col-md-4 col-lg-2\">"
             + "<a href=\"/restaurants\" class=\"text-decoration-none\">"
             + "<div class=\"card text-center\" style=\"cursor:pointer\">"
             + "<img src=\"" + img + "\" style=\"height:90px;object-fit:cover;width:100%\" alt=\"" + name + "\"/>"
             + "<div class=\"py-2 fw-600\" style=\"font-size:.85rem\">" + name + "</div>"
             + "</div></a></div>";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
