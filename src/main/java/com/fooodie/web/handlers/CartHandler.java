package com.fooodie.web.handlers;

import com.fooodie.services.CartStore;
import com.fooodie.services.CartStore.CartItem;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;
import com.fooodie.web.template.HtmlPage;

import java.util.Map;

public class CartHandler implements RouteHandler {

    @Override
    public Response handle(RequestContext ctx) {
        UserSession session = SessionManager.getSession(ctx);
        if (session == null) return Response.redirect("/login");

        String token = ctx.cookie("FOOODIE_SESSION");
        Map<Long, CartItem> cart = CartStore.getCart(token);
        int cartCount = cart.size();

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"section-title\">Your Cart</div>");

        if (cart.isEmpty()) {
            sb.append("<div class=\"card\"><div class=\"card-body text-center py-5\">")
              .append("<img src=\"https://images.unsplash.com/photo-1586190848861-99aa4a171e90?w=300&q=80\" style=\"width:180px;border-radius:12px;opacity:.7\" class=\"mb-4\"/>")
              .append("<h5 class=\"fw-bold\">Your cart is empty</h5>")
              .append("<p class=\"text-muted\">Add some delicious items from our restaurants.</p>")
              .append("<a href=\"/restaurants\" class=\"btn btn-brand px-4\">Browse Restaurants</a>")
              .append("</div></div>");
        } else {
            double subtotal = CartStore.total(token);
            double delivery = 2.99;
            double total = subtotal + delivery;

            sb.append("<div class=\"row g-4\">");

            // Items list
            sb.append("<div class=\"col-lg-8\">");
            sb.append("<div class=\"card\"><div class=\"card-body p-0\">");
            sb.append("<table class=\"table cart-table mb-0\">");
            sb.append("<thead style=\"background:#f8f9fc\"><tr>")
              .append("<th style=\"padding-left:1.25rem\">Item</th>")
              .append("<th>Price</th><th>Qty</th><th>Subtotal</th><th></th>")
              .append("</tr></thead><tbody>");

            for (CartItem ci : cart.values()) {
                sb.append("<tr>")
                  .append("<td style=\"padding-left:1.25rem\">")
                  .append("<div class=\"d-flex align-items-center gap-3\">")
                  .append("<img src=\"").append(ci.item.getImageUrl()).append("\" style=\"width:52px;height:52px;border-radius:8px;object-fit:cover\"/>")
                  .append("<div><div class=\"fw-600\">").append(esc(ci.item.getName())).append("</div>")
                  .append("<small class=\"text-muted\">").append(esc(ci.item.getCategory())).append("</small></div>")
                  .append("</div></td>")
                  .append("<td>$").append(String.format("%.2f", ci.item.getPrice())).append("</td>")
                  .append("<td><span class=\"badge\" style=\"background:var(--brand-light);color:var(--brand);font-size:.85rem;padding:4px 10px\">").append(ci.qty).append("</span></td>")
                  .append("<td class=\"fw-600\">$").append(String.format("%.2f", ci.item.getPrice() * ci.qty)).append("</td>")
                  .append("<td><form method=\"post\" action=\"/cart/remove\">")
                  .append("<input type=\"hidden\" name=\"itemId\" value=\"").append(ci.item.getId()).append("\"/>")
                  .append("<button class=\"btn btn-sm\" style=\"color:#ef4444;background:#fee2e2;border:0;border-radius:8px\">Remove</button>")
                  .append("</form></td></tr>");
            }
            sb.append("</tbody></table></div></div></div>");

            // Order summary
            sb.append("<div class=\"col-lg-4\">");
            sb.append("<div class=\"card\"><div class=\"card-body p-4\">");
            sb.append("<h6 class=\"fw-bold mb-3\">Order Summary</h6>");
            sb.append("<div class=\"d-flex justify-content-between mb-2\"><span class=\"text-muted\">Subtotal</span><span>$").append(String.format("%.2f", subtotal)).append("</span></div>");
            sb.append("<div class=\"d-flex justify-content-between mb-2\"><span class=\"text-muted\">Delivery fee</span><span>$").append(String.format("%.2f", delivery)).append("</span></div>");
            sb.append("<hr/>");
            sb.append("<div class=\"d-flex justify-content-between mb-3\"><span class=\"fw-bold\">Total</span><span class=\"fw-bold\" style=\"color:var(--brand);font-size:1.1rem\">$").append(String.format("%.2f", total)).append("</span></div>");
            sb.append("<form method=\"post\" action=\"/checkout\">")
              .append("<button class=\"btn btn-brand w-100 py-2\">Place Order</button>")
              .append("</form>");
            sb.append("<a href=\"/restaurants\" class=\"btn btn-outline-brand w-100 mt-2\">Add More Items</a>");
            sb.append("</div></div></div>");

            sb.append("</div>");
        }

        return Response.html(200, HtmlPage.layout("Cart", sb.toString(), "/cart",
                session.username, cartCount));
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}
