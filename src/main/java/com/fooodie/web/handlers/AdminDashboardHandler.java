package com.fooodie.web.handlers;

import com.fooodie.db.OrderDao;
import com.fooodie.db.RestaurantDao;
import com.fooodie.model.Restaurant;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

import java.util.List;

public class AdminDashboardHandler implements RouteHandler {

    private final RestaurantDao restaurantDao = new RestaurantDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        String username = session != null ? session.username : "Guest";

        List<Restaurant> restaurants = restaurantDao.findAll();
        int menuCount = restaurants.stream().mapToInt(r -> r.getMenuItems().size()).sum();
        int orderCount = orderDao.totalOrderCount();

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"d-flex justify-content-between align-items-center mb-4\">")
          .append("<div class=\"section-title mb-0\">Admin Dashboard</div>")
          .append("<span class=\"cat-pill\">Signed in as ").append(esc(username)).append("</span></div>");

        sb.append("<div class=\"row g-3 mb-4\">");
        sb.append(stat("Restaurants", String.valueOf(restaurants.size()), "#ff6b35"));
        sb.append(stat("Menu Items",  String.valueOf(menuCount),          "#8b5cf6"));
        sb.append(stat("Total Orders", String.valueOf(orderCount),        "#10b981"));
        sb.append(stat("Active Users", "2",                               "#3b82f6"));
        sb.append("</div>");

        sb.append("<div class=\"card\"><div class=\"card-body p-0\">")
          .append("<div class=\"p-4 pb-2\"><h6 class=\"fw-bold mb-0\">Restaurants in DB</h6></div>")
          .append("<table class=\"table mb-0\" style=\"font-size:.9rem\">")
          .append("<thead style=\"background:#f8f9fc\"><tr>")
          .append("<th style=\"padding-left:1.25rem\">ID</th><th>Name</th>")
          .append("<th>Rating</th><th>Delivery Fee</th><th>ETA</th><th>Menu Items</th><th>Status</th>")
          .append("</tr></thead><tbody>");

        for (Restaurant r : restaurants) {
            sb.append("<tr>")
              .append("<td style=\"padding-left:1.25rem;color:var(--muted)\">").append(r.getId()).append("</td>")
              .append("<td><span class=\"fw-600\">").append(esc(r.getName())).append("</span><br>")
              .append("<small class=\"text-muted\">").append(esc(r.getAddress())).append("</small></td>")
              .append("<td>").append(r.getRating()).append("</td>")
              .append("<td>$").append(String.format("%.2f", r.getDeliveryFee())).append("</td>")
              .append("<td>").append(r.getEstimatedDeliveryTime()).append(" min</td>")
              .append("<td>").append(r.getMenuItems().size()).append("</td>")
              .append("<td><span class=\"open-tag\">Open</span></td></tr>");
        }

        sb.append("</tbody></table></div></div>");

        return Response.html(200, HtmlPage.layout("Admin", sb.toString(), "/admin", username, null));
    }

    private static String stat(String label, String value, String color) {
        return "<div class=\"col-6 col-md-3\"><div class=\"card\"><div class=\"card-body p-3 text-center\">"
             + "<div style=\"font-size:1.9rem;font-weight:800;color:" + color + "\">" + value + "</div>"
             + "<div class=\"text-muted\" style=\"font-size:.8rem\">" + label + "</div>"
             + "</div></div></div>";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
