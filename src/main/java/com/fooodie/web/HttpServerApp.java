package com.fooodie.web;

import com.fooodie.web.handlers.*;
import com.fooodie.web.middleware.AuthMiddleware;
import com.fooodie.web.middleware.RouteHandler;
import com.fooodie.web.middleware.Router;

/**
 * Minimal HTTP server (no Spring) to serve HTML pages and handle form submissions.
 *
 * Endpoints are wired in {@link #main(String[])}.
 */
public class HttpServerApp {

    public static void main(String[] args) throws Exception {
        int port = 9090;
        Router router = new Router(port);

        router.get("/", new HomeHandler());
        router.get("/home", new DashboardHandler());
        router.get("/login", new LoginPageHandler());
        router.post("/login", new LoginHandler());
        router.get("/register", new RegisterPageHandler());
        router.post("/register", new RegisterHandler());
        router.get("/logout", new LogoutHandler());
        router.get("/restaurants", new RestaurantsHandler());
        router.post("/cart/add", new CartAddHandler());
        router.post("/cart/remove", new CartRemoveHandler());
        router.get("/cart", new CartHandler());
        router.post("/checkout", new CheckoutHandler());
        router.get("/orders", new OrdersHandler());
        router.get("/profile", new ProfileHandler());

        router.start();
        System.out.println("Fooodie running on http://localhost:" + port);
    }

}

