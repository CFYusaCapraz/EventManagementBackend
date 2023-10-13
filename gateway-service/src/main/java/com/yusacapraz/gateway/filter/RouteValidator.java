package com.yusacapraz.gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints =
            List.of(
                    "/api/auth/login",
                    "/api/auth/validateToken"
            );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(s -> request.getURI().getPath().contains(s));
}
