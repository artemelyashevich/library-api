package com.elyashevich.gateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private static final List<String> ENDPOINTS = List.of(
            "api/v1/auth/register",
            "api/v1/auth/login",
            "api/v1/eureka/**"
    );

    private RouteValidator() {
    }

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> ENDPOINTS
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}