package com.fooodie.web.resp;

import java.util.HashMap;
import java.util.Map;

public class Response {

    public final int statusCode;
    public final String body;
    public final String contentType;
    public final Map<String, String> headers;

    private Response(int statusCode, String body, String contentType) {
        this.statusCode = statusCode;
        this.body = body;
        this.contentType = contentType;
        this.headers = new HashMap<>();
    }

    public static Response text(int status, String body) {
        return new Response(status, body, "text/plain; charset=utf-8");
    }

    public static Response html(int status, String html) {
        return new Response(status, html, "text/html; charset=utf-8");
    }

    public static Response redirect(String location) {
        Response r = new Response(302, "", "text/plain; charset=utf-8");
        r.headers.put("Location", location);
        return r;
    }
}

