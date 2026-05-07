package com.fooodie.web.middleware;

import com.fooodie.web.req.RequestContext;
import com.fooodie.web.resp.Response;

@FunctionalInterface
public interface RouteHandler {
    Response handle(RequestContext ctx) throws Exception;
}

