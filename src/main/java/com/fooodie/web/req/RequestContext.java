package com.fooodie.web.req;

import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper for reading parameters (query/body) and cookies.
 */
public class RequestContext {

    private final HttpExchange exchange;
    private final String path;
    private Map<String, String> queryParams;
    private Map<String, String> formParams;

    public RequestContext(HttpExchange exchange, String path) {
        this.exchange = exchange;
        this.path = path;
    }

    public HttpExchange exchange() {
        return exchange;
    }

    public String path() {
        return path;
    }

    public String queryParam(String key) {
        ensureQuery();
        return queryParams.get(key);
    }

    public String formParam(String key) {
        ensureForm();
        return formParams.get(key);
    }

    public Map<String, String> query() {
        ensureQuery();
        return queryParams;
    }

    public Map<String, String> form() {
        ensureForm();
        return formParams;
    }

    public String cookie(String name) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookieHeader == null) return null;
        String[] parts = cookieHeader.split(";");
        for (String p : parts) {
            String s = p.trim();
            if (s.startsWith(name + "=")) {
                return s.substring((name + "=").length());
            }
        }
        return null;
    }

    private void ensureQuery() {
        if (queryParams != null) return;
        queryParams = new HashMap<>();
        String rawQuery = exchange.getRequestURI().getRawQuery();
        if (rawQuery == null || rawQuery.isEmpty()) return;
        for (String pair : rawQuery.split("&")) {
            int idx = pair.indexOf('=');
            String k = idx >= 0 ? pair.substring(0, idx) : pair;
            String v = idx >= 0 ? pair.substring(idx + 1) : "";
            queryParams.put(urlDecode(k), urlDecode(v));
        }
    }

    private void ensureForm() throws RuntimeException {
        if (formParams != null) return;
        formParams = new HashMap<>();
        String ctype = exchange.getRequestHeaders().getFirst("Content-Type");
        if (ctype != null && ctype.contains("application/x-www-form-urlencoded")) {
            try {
                String body = readBodyAsString();
                if (!body.isEmpty()) {
                    for (String pair : body.split("&")) {
                        int idx = pair.indexOf('=');
                        String k = idx >= 0 ? pair.substring(0, idx) : pair;
                        String v = idx >= 0 ? pair.substring(idx + 1) : "";
                        formParams.put(urlDecode(k), urlDecode(v));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String readBodyAsString() throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int r;
            while ((r = is.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
            return baos.toString(StandardCharsets.UTF_8);
        }
    }

    private String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }
}

