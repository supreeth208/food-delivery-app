package com.fooodie.web.handlers;

import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.template.HtmlPage;

public class RegisterPageHandler implements RouteHandler {
    @Override
    public Response handle(RequestContext ctx) {
        String body =
            "<div class=\"row justify-content-center\">"
          + "<div class=\"col-md-10 col-lg-8\">"
          + "<div class=\"row g-0 card overflow-hidden\">"

          // Left panel
          + "<div class=\"col-md-5 d-none d-md-flex flex-column justify-content-center p-5\" "
          + "style=\"background:linear-gradient(160deg,#1a1a2e,#0f3460);color:#fff\">"
          + "<div style=\"font-size:2rem;font-weight:800;color:var(--brand)\">Fooodie</div>"
          + "<p class=\"mt-3\" style=\"opacity:.8\">Join thousands of food lovers ordering daily.</p>"
          + "<ul class=\"mt-3\" style=\"opacity:.75;padding-left:1.2rem;font-size:.9rem\">"
          + "<li>Free to join</li><li>Exclusive deals</li><li>Fast checkout</li>"
          + "</ul></div>"

          // Right panel — form
          + "<div class=\"col-md-7\"><div class=\"card-body p-5\">"
          + "<h4 class=\"fw-bold mb-1\">Create account</h4>"
          + "<p class=\"text-muted mb-4\" style=\"font-size:.9rem\">It's free and takes 30 seconds</p>"
          + "<form method=\"post\" action=\"/register\">"
          + field("Full Name", "fullName", "text", "2")
          + field("Email", "email", "email", null)
          + field("Username", "username", "text", "3")
          + field("Password", "password", "password", "6")
          + "<button class=\"btn btn-brand w-100 py-2\">Create Account</button>"
          + "</form>"
          + "<p class=\"text-center mt-3 mb-0\" style=\"font-size:.88rem\">Already have an account? <a href=\"/login\" style=\"color:var(--brand);font-weight:600\">Sign in</a></p>"
          + "</div></div>"

          + "</div></div></div>";

        return Response.html(200, HtmlPage.layout("Register", body, "/register"));
    }

    private static String field(String label, String name, String type, String minLen) {
        String min = minLen != null ? " minlength=\"" + minLen + "\"" : "";
        return "<div class=\"mb-3\">"
             + "<label class=\"form-label fw-500\">" + label + "</label>"
             + "<input name=\"" + name + "\" type=\"" + type + "\" class=\"form-control\" "
             + "style=\"border-radius:10px;padding:.65rem 1rem\" required" + min + "/>"
             + "</div>";
    }
}
