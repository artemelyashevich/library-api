package com.elyashevich.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@Component
public class CustomAuthenticationFilter extends AbstractGatewayFilterFactory<CustomAuthenticationFilter.Config> {

    private static final String INVALID_ACCESS_MESSAGE = "Invalid access";

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String MISSING_JWT_TOKEN = "Invalid or missing JWT token";

    private final WebClient webClient;

    @Autowired
    public CustomAuthenticationFilter(final WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient.build();
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            if (!RouteValidator.isSecured.test(exchange.getRequest())) {
                return chain.filter(exchange);
            }
            var authorizationHeaders = exchange
                    .getRequest()
                    .getHeaders()
                    .getOrDefault(AUTHORIZATION_HEADER, Collections.emptyList());
            var jwt = authorizationHeaders.stream()
                    .filter(Objects::nonNull)
                    .filter(header -> header.trim().startsWith("Bearer "))
                    .map(header -> header.substring(7))
                    .findFirst()
                    .orElse(null);
            return jwt != null
                    ? validateToken(exchange, chain, jwt)
                    : handleInvalidAccess(exchange, MISSING_JWT_TOKEN);
        };
    }

    private Mono<Void> validateToken(
            final ServerWebExchange exchange,
            final GatewayFilterChain chain,
            final String token
    ) {
        return this.webClient.post()
                .uri("http://localhost:8083/api/v1/auth/%s".formatted(token))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> handleInvalidAccess(exchange, INVALID_ACCESS_MESSAGE)
                                .then(Mono.error(new RuntimeException("Unauthorized access")))
                )
                .bodyToMono(String.class)
                .flatMap(response -> chain.filter(exchange));
    }

    private Mono<Void> handleInvalidAccess(final ServerWebExchange exchange, final String errorMessage) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var res = Mono.just(response.bufferFactory().wrap(errorMessage.getBytes()));
        return response.writeWith(res);
    }

    public static class Config {
    }
}