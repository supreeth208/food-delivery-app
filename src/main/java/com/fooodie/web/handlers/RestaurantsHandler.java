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
import com.fooodie.web.template.HtmlPage;

import java.util.List;

public class RestaurantsHandler implements RouteHandler {

    private static final String[] COVERS = {
        "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
        "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&q=80",
        "https://images.unsplash.com/photo-1550547660-d9450f859349?w=800&q=80",
        "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800&q=80",
        "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=800&q=80",
        "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=800&q=80"
    };

    private final RestaurantDao dao = new RestaurantDao();

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        String token = ctx.cookie("FOOODIE_SESSION");
        int cartCount = token != null ? CartStore.getCart(token).size() : 0;

        List<Restaurant> restaurants = dao.findAll();

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"d-flex align-items-center justify-content-between mb-4\">")
          .append("<div><div class=\"section-title mb-0\">All Restaurants</div>")
          .append("<p class=\"text-muted mb-0\" style=\"font-size:.9rem\">")
          .append(restaurants.size()).append(" places to eat</p></div></div>");

        sb.append("<div class=\"row g-4\">");
        int idx = 0;
        for (Restaurant r : restaurants) {
            String cover = COVERS[idx % COVERS.length]; idx++;
            sb.append("<div class=\"col-lg-6\"><div class=\"card h-100\">");
            sb.append("<div style=\"position:relative\">")
              .append("<img src=\"").append(cover).append("\" class=\"rest-cover\" alt=\"").append(esc(r.getName())).append("\"/>")
              .append("<div style=\"position:absolute;top:12px;left:12px\"><span class=\"open-tag\">Open</span></div>")
              .append("<div style=\"position:absolute;top:12px;right:12px\"><span class=\"rest-badge\">").append(r.getEstimatedDeliveryTime()).append(" min</span></div>")
              .append("</div>");

            sb.append("<div class=\"card-body p-4\">")
              .append("<div class=\"d-flex justify-content-between align-items-start mb-1\">")
              .append("<h5 class=\"mb-0 fw-bold\">").append(esc(r.getName())).append("</h5>")
              .append("<div class=\"stars\">").append(stars(r.getRating()))
              .append(" <span style=\"color:var(--muted);font-size:.8rem\">").append(r.getRating()).append("</span></div></div>")
              .append("<p class=\"text-muted mb-2\" style=\"font-size:.88rem\">").append(esc(r.getDescription())).append("</p>")
              .append("<div class=\"rest-meta mb-3\">")
              .append("<span>Delivery: <b>$").append(String.format("%.2f", r.getDeliveryFee())).append("</b></span>")
              .append("<span>ETA: <b>").append(r.getEstimatedDeliveryTime()).append(" min</b></span>")
              .append("<span>").append(esc(r.getAddress())).append("</span></div>");

            sb.append("<div class=\"d-flex align-items-center mb-2\"><span class=\"fw-600\" style=\"font-size:.95rem\">Menu</span><hr class=\"flex-grow-1 ms-2 my-0\"/></div>");
            sb.append("<div class=\"d-flex flex-column gap-2\">");
            for (MenuItem m : r.getMenuItems()) {
                sb.append("<div class=\"menu-card\">")
                  .append("<img src=\"").append(m.getImageUrl()).append("\" class=\"menu-img\" alt=\"").append(esc(m.getName())).append("\"/>")
                  .append("<div class=\"flex-grow-1 min-w-0\">")
                  .append("<div class=\"cat-pill\">").append(esc(m.getCategory())).append("</div>")
                  .append("<div class=\"menu-name\">").append(esc(m.getName())).append("</div>")
                  .append("<div class=\"menu-desc\">").append(esc(m.getDescription())).append("</div></div>")
                  .append("<div class=\"d-flex flex-column align-items-end gap-1 ms-2\">")
                  .append("<div class=\"menu-price\">$").append(String.format("%.2f", m.getPrice())).append("</div>");
                if (session != null) {
                    sb.append("<form method=\"post\" action=\"/cart/add\">")
                      .append("<input type=\"hidden\" name=\"itemId\" value=\"").append(m.getId()).append("\"/>")
                      .append("<input type=\"hidden\" name=\"restaurantId\" value=\"").append(r.getId()).append("\"/>")
                      .append("<button class=\"btn btn-brand\" style=\"font-size:.78rem;padding:4px 12px\">+ Add</button></form>");
                } else {
                    sb.append("<a href=\"/login\" class=\"btn btn-outline-brand\" style=\"font-size:.78rem;padding:4px 12px\">Login</a>");
                }
                sb.append("</div></div>");
            }
            sb.append("</div></div></div></div>");
        }
        sb.append("</div>");

        return Response.html(200, HtmlPage.layout("Restaurants", sb.toString(), "/restaurants",
                session != null ? session.username : null, cartCount));
    }

    private static String stars(double rating) {
        int full = (int) Math.round(rating);
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 5; i++) s.append(i < full ? "&#9733;" : "&#9734;");
        return s.toString();
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
