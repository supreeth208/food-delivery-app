package com.fooodie.web.handlers;

import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.template.HtmlPage;

public class LoginPageHandler implements RouteHandler {
    @Override
    public Response handle(RequestContext ctx) {
        String error = ctx.queryParam("error");
        String errorHtml = error != null
                ? "<div class=\"alert\" style=\"background:#fee2e2;color:#991b1b;border:0;border-radius:10px;padding:.75rem 1rem;margin-bottom:1rem\">Invalid username or password.</div>"
                : "";

        String body =
            "<div class=\"row justify-content-center\">"
          + "<div class=\"col-md-10 col-lg-8\">"
          + "<div class=\"row g-0 card overflow-hidden\">"

          // Left panel
          + "<div class=\"col-md-5 d-none d-md-flex flex-column justify-content-center p-5\" "
          + "style=\"background:linear-gradient(160deg,#1a1a2e,#0f3460);color:#fff\">"
          + "<div style=\"font-size:2rem;font-weight:800;color:var(--brand)\">Fooodie</div>"
          + "<p class=\"mt-3\" style=\"opacity:.8\">Your favourite food, delivered fast.</p>"
          + "<div class=\"mt-4 p-3\" style=\"background:rgba(255,255,255,.08);border-radius:12px\">"
          + "<div style=\"font-size:.8rem;opacity:.7\">Demo credentials</div>"
          + "<div class=\"mt-1\"><b>admin</b> / admin123</div>"
          + "<div><b>customer</b> / customer123</div>"
          + "</div></div>"

          // Right panel — form
          + "<div class=\"col-md-7\"><div class=\"card-body p-5\">"
          + "<h4 class=\"fw-bold mb-1\">Welcome back</h4>"
          + "<p class=\"text-muted mb-4\" style=\"font-size:.9rem\">Sign in to your account</p>"
          + errorHtml
          + "<form method=\"post\" action=\"/login\">"
          + "<div class=\"mb-3\">"
          + "<label class=\"form-label fw-500\">Username</label>"
          + "<input name=\"username\" class=\"form-control\" style=\"border-radius:10px;padding:.65rem 1rem\" required/>"
          + "</div>"
          + "<div class=\"mb-4\">"
          + "<label class=\"form-label fw-500\">Password</label>"
          + "<input name=\"password\" type=\"password\" class=\"form-control\" style=\"border-radius:10px;padding:.65rem 1rem\" required/>"
          + "</div>"
          + "<button class=\"btn btn-brand w-100 py-2\">Sign In</button>"
          + "</form>"
          + "<p class=\"text-center mt-3 mb-0\" style=\"font-size:.88rem\">No account? <a href=\"/register\" style=\"color:var(--brand);font-weight:600\">Create one</a></p>"
          + "</div></div>"

          + "</div></div></div>";

        return Response.html(200, HtmlPage.layout("Login", body, "/login"));
    }
}
