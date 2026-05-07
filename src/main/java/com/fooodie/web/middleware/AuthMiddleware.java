package com.fooodie.web.middleware;

import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.fooodie.web.session.SessionManager;
import com.fooodie.web.session.UserSession;

/**
 * Simple role-based authorization middleware.
 */
public class AuthMiddleware {

    private final Router router;
    private final String requiredRole;

    public AuthMiddleware(Router router, String requiredRole) {
        this.router = router;
        this.requiredRole = requiredRole;
    }

    public RouteHandler wrap(RouteHandler next) {
        return (RequestContext ctx) -> {
            UserSession session = SessionManager.getSession(ctx);
            if (session == null || session.role == null) {
                return Response.redirect("/login");
            }
            if (!requiredRole.equalsIgnoreCase(session.role)) {
                return Response.text(403, "Forbidden");
            }
            return next.handle(ctx);
        };
    }
}

