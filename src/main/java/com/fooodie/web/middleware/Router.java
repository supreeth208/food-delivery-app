package com.fooodie.web.middleware;

import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Very small router with wildcard support using '*' suffix.
 */
public class Router {

    private final HttpServer server;
    private final int port;
    private final List<Route> routes = new ArrayList<>();

    public Router(int port) throws IOException {
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);
        this.server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/", this::dispatch);
    }

    public void get(String pathPattern, RouteHandler handler) {
        routes.add(new Route("GET", pathPattern, handler));
    }

    public void post(String pathPattern, RouteHandler handler) {
        routes.add(new Route("POST", pathPattern, handler));
    }

    public void start() {
        server.start();
    }

    private void dispatch(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            for (Route r : routes) {
                if (!r.method.equalsIgnoreCase(method)) continue;
                if (match(r.pathPattern, path)) {
                    RequestContext ctx = new RequestContext(exchange, path);
                    Response resp = r.handler.handle(ctx);
                    write(exchange, resp);
                    return;
                }
            }

            Response notFound = Response.text(404, "Not Found");
            write(exchange, notFound);
        } catch (Exception e) {
            e.printStackTrace();
            Response err = Response.text(500, "Server Error: " + e.getMessage());
            write(exchange, err);
        }
    }

    private boolean match(String pattern, String path) {
        if (pattern.equals(path)) return true;
        // wildcard: /restaurants/* matches /restaurants/123
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 1); // keep trailing slash
            return path.startsWith(prefix);
        }
        return false;
    }

    private void write(HttpExchange exchange, Response resp) throws IOException {
        byte[] body = resp.body == null ? new byte[0] : resp.body.getBytes(StandardCharsets.UTF_8);
        resp.headers.forEach((k, v) -> exchange.getResponseHeaders().set(k, v));
        exchange.getResponseHeaders().set("Content-Type", resp.contentType);
        exchange.sendResponseHeaders(resp.statusCode, body.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body);
        }
    }

    private static class Route {
        final String method;
        final String pathPattern;
        final RouteHandler handler;

        Route(String method, String pathPattern, RouteHandler handler) {
            this.method = method;
            this.pathPattern = pathPattern;
            this.handler = handler;
        }
    }
}

