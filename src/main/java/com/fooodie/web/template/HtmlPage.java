package com.fooodie.web.template;

public class HtmlPage {

    public static String layout(String title, String body, String activePath) {
        return layout(title, body, activePath, null, null);
    }

    public static String layout(String title, String body, String activePath,
                                 String username, Integer cartCount) {
        String t = esc(title);

        String cartBadge = (cartCount != null && cartCount > 0)
                ? " <span class=\"badge bg-warning text-dark ms-1\">" + cartCount + "</span>" : "";

        // Center nav — only shown when logged in
        String centerNav = username != null
                ? "        <li class=\"nav-item\"><a class=\"nav-link" + active(activePath, "/home") + "\" href=\"/home\">Home</a></li>\n"
                + "        <li class=\"nav-item\"><a class=\"nav-link" + active(activePath, "/restaurants") + "\" href=\"/restaurants\">Restaurants</a></li>\n"
                + "        <li class=\"nav-item\"><a class=\"nav-link" + active(activePath, "/cart") + "\" href=\"/cart\">Cart" + cartBadge + "</a></li>\n"
                + "        <li class=\"nav-item\"><a class=\"nav-link" + active(activePath, "/orders") + "\" href=\"/orders\">Orders</a></li>\n"
                : "";

        // Right nav — avatar dropdown when logged in, login/signup when not
        String rightNav = username != null
                ? "<li class=\"nav-item dropdown\">"
                + "<a class=\"nav-link dropdown-toggle d-flex align-items-center gap-1\" href=\"#\" data-bs-toggle=\"dropdown\">"
                + "<span class=\"avatar\">" + esc(username.substring(0, 1).toUpperCase()) + "</span>"
                + esc(username) + "</a>"
                + "<ul class=\"dropdown-menu dropdown-menu-end\">"
                + "<li><a class=\"dropdown-item\" href=\"/profile\">Profile</a></li>"
                + "<li><a class=\"dropdown-item\" href=\"/orders\">My Orders</a></li>"
                + "<li><hr class=\"dropdown-divider\"></li>"
                + "<li><a class=\"dropdown-item text-danger\" href=\"/logout\">Logout</a></li>"
                + "</ul></li>"
                : "<li class=\"nav-item\"><a class=\"nav-link" + active(activePath, "/login") + "\" href=\"/login\">Login</a></li>"
                + "<li class=\"nav-item\"><a class=\"btn btn-warning btn-sm ms-2 fw-semibold\" href=\"/register\">Sign Up</a></li>";

        return "<!doctype html>\n"
            + "<html lang=\"en\">\n"
            + "<head>\n"
            + "  <meta charset=\"utf-8\"/>\n"
            + "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"/>\n"
            + "  <title>" + t + " — Fooodie</title>\n"
            + "  <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
            + "  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap\" rel=\"stylesheet\">\n"
            + "  <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\"></script>\n"
            + "  <style>\n"
            + "    :root{--brand:#ff6b35;--brand-dark:#e85520;--brand-light:#fff4f0;--surface:#ffffff;--bg:#f8f9fc;--text:#1a1a2e;--muted:#6b7280;--radius:14px;--shadow:0 4px 24px rgba(0,0,0,.08);}\n"
            + "    *{box-sizing:border-box;}\n"
            + "    body{font-family:'Inter',sans-serif;background:var(--bg);color:var(--text);margin:0;}\n"
            + "    .navbar{background:var(--surface)!important;box-shadow:0 2px 12px rgba(0,0,0,.07);padding:.75rem 0;}\n"
            + "    .navbar-brand{font-weight:800;font-size:1.5rem;color:var(--brand)!important;letter-spacing:-0.5px;}\n"
            + "    .navbar-brand span{color:var(--text);}\n"
            + "    .nav-link{color:var(--muted)!important;font-weight:500;transition:color .2s;padding:.4rem .8rem!important;border-radius:8px;}\n"
            + "    .nav-link:hover,.nav-link.active{color:var(--brand)!important;background:var(--brand-light);}\n"
            + "    .avatar{width:28px;height:28px;border-radius:50%;background:var(--brand);color:#fff;display:inline-flex;align-items:center;justify-content:center;font-size:.75rem;font-weight:700;}\n"
            + "    .card{border:0!important;border-radius:var(--radius)!important;box-shadow:var(--shadow);transition:transform .2s,box-shadow .2s;overflow:hidden;}\n"
            + "    .card:hover{transform:translateY(-4px);box-shadow:0 12px 32px rgba(0,0,0,.13);}\n"
            + "    .rest-cover{width:100%;height:180px;object-fit:cover;}\n"
            + "    .rest-badge{background:var(--brand);color:#fff;font-size:.7rem;font-weight:700;padding:2px 8px;border-radius:20px;}\n"
            + "    .rest-meta span{font-size:.8rem;color:var(--muted);margin-right:.75rem;}\n"
            + "    .menu-card{display:flex;gap:12px;align-items:center;background:var(--surface);border-radius:12px;padding:12px;box-shadow:0 2px 10px rgba(0,0,0,.05);transition:box-shadow .2s;}\n"
            + "    .menu-card:hover{box-shadow:0 6px 20px rgba(0,0,0,.1);}\n"
            + "    .menu-img{width:72px;height:72px;border-radius:10px;object-fit:cover;flex-shrink:0;}\n"
            + "    .menu-name{font-weight:600;font-size:.95rem;margin-bottom:2px;}\n"
            + "    .menu-desc{font-size:.78rem;color:var(--muted);line-height:1.4;}\n"
            + "    .menu-price{font-weight:700;color:var(--brand);font-size:1rem;white-space:nowrap;}\n"
            + "    .btn-brand{background:var(--brand);color:#fff;border:0;border-radius:10px;font-weight:600;padding:.45rem 1.1rem;transition:background .2s,transform .1s;}\n"
            + "    .btn-brand:hover{background:var(--brand-dark);color:#fff;transform:scale(1.03);}\n"
            + "    .btn-outline-brand{border:2px solid var(--brand);color:var(--brand);background:transparent;border-radius:10px;font-weight:600;padding:.4rem 1rem;transition:all .2s;}\n"
            + "    .btn-outline-brand:hover{background:var(--brand);color:#fff;}\n"
            + "    .hero{background:linear-gradient(135deg,#1a1a2e 0%,#16213e 50%,#0f3460 100%);border-radius:20px;padding:3.5rem 2.5rem;color:#fff;position:relative;overflow:hidden;}\n"
            + "    .hero::before{content:'';position:absolute;top:-60px;right:-60px;width:300px;height:300px;background:radial-gradient(circle,rgba(255,107,53,.35) 0%,transparent 70%);}\n"
            + "    .hero::after{content:'';position:absolute;bottom:-80px;left:-40px;width:250px;height:250px;background:radial-gradient(circle,rgba(255,200,0,.15) 0%,transparent 70%);}\n"
            + "    .hero h1{font-size:2.6rem;font-weight:800;line-height:1.15;}\n"
            + "    .hero p{opacity:.8;font-size:1.1rem;}\n"
            + "    .cat-pill{display:inline-block;background:var(--brand-light);color:var(--brand);font-size:.7rem;font-weight:600;padding:2px 10px;border-radius:20px;margin-bottom:6px;}\n"
            + "    .section-title{font-size:1.5rem;font-weight:800;margin-bottom:1.25rem;}\n"
            + "    .cart-table td,.cart-table th{vertical-align:middle;padding:.75rem 1rem;}\n"
            + "    .status-badge{font-size:.72rem;font-weight:700;padding:4px 12px;border-radius:20px;background:var(--brand-light);color:var(--brand);}\n"
            + "    .stars{color:#f59e0b;font-size:.85rem;}\n"
            + "    .open-tag{background:#d1fae5;color:#065f46;font-size:.7rem;font-weight:700;padding:2px 8px;border-radius:20px;}\n"
            + "    .closed-tag{background:#fee2e2;color:#991b1b;font-size:.7rem;font-weight:700;padding:2px 8px;border-radius:20px;}\n"
            + "    ::-webkit-scrollbar{width:6px;} ::-webkit-scrollbar-thumb{background:#ddd;border-radius:3px;}\n"
            + "    @keyframes fadeUp{from{opacity:0;transform:translateY(16px)}to{opacity:1;transform:none}}\n"
            + "    .fade-up{animation:fadeUp .4s ease both;}\n"
            + "  </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "<nav class=\"navbar navbar-expand-lg\">\n"
            + "  <div class=\"container\">\n"
            + "    <a class=\"navbar-brand\" href=\"/\">Fooo<span>die</span></a>\n"
            + "    <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#nav\" style=\"border-color:var(--brand)\">\n"
            + "      <span class=\"navbar-toggler-icon\"></span>\n"
            + "    </button>\n"
            + "    <div id=\"nav\" class=\"collapse navbar-collapse\">\n"
            + "      <ul class=\"navbar-nav mx-auto mb-2 mb-lg-0 gap-1\">\n"
            + centerNav
            + "      </ul>\n"
            + "      <ul class=\"navbar-nav mb-2 mb-lg-0 align-items-center gap-1\">\n"
            + rightNav + "\n"
            + "      </ul>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</nav>\n"
            + "<div class=\"container py-4 fade-up\">\n"
            + (body == null ? "" : body) + "\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>\n";
    }

    private static String active(String current, String path) {
        return (current != null && current.equals(path)) ? " active" : "";
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
