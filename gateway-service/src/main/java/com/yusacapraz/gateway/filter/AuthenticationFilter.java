package com.yusacapraz.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yusacapraz.gateway.model.APIResponse;
import com.yusacapraz.gateway.model.DTOs.ValidateRequestDTO;
import com.yusacapraz.gateway.service.GatewayService;
import com.yusacapraz.gateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouteValidator routeValidator;
    private final JwtUtils jwtUtils;
    private final GatewayService gatewayService;

    @Autowired
    public AuthenticationFilter(RouteValidator routeValidator, JwtUtils jwtUtils, GatewayService gatewayService) {
        super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtUtils = jwtUtils;
        this.gatewayService = gatewayService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    ObjectMapper objectMapper = new ObjectMapper();
                    APIResponse<Object> response = APIResponse.error("Missing `Authorization` header!");
                    return serializeResponseToJSON(response, objectMapper, exchange)
                            .flatMap(jsonResponse -> exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes())))
                                    .then(Mono.empty()));
                }
                String token = jwtUtils.resolveToken(exchange.getRequest());
                if (token == null) {
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    ObjectMapper objectMapper = new ObjectMapper();
                    APIResponse<Object> response = APIResponse.error("Missing `Bearer` prefix in the authorization header value!");
                    return serializeResponseToJSON(response, objectMapper, exchange)
                            .flatMap(jsonResponse -> exchange.getResponse()
                                    .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes())))
                                    .then(Mono.empty()));
                }
                ValidateRequestDTO validateRequestDTO = ValidateRequestDTO.builder()
                        .token(token)
                        .requestPath(exchange.getRequest().getPath().toString()).build();

                return gatewayService.authenticateToken(validateRequestDTO)
                        .flatMap(responseEntity -> {
                            if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                                return chain.filter(exchange);
                            } else {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                ObjectMapper objectMapper = new ObjectMapper();

                                return serializeResponseToJSON(responseEntity.getBody(), objectMapper, exchange)
                                        .flatMap(jsonResponse -> exchange.getResponse()
                                                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes())))
                                                .then(Mono.empty()));
                            }
                        })
                        .onErrorResume(WebClientResponseException.class, e -> {
                            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                ObjectMapper objectMapper = new ObjectMapper();

                                return serializeResponseToJSON(e.getResponseBodyAsByteArray(), objectMapper, exchange)
                                        .flatMap(jsonResponse -> exchange.getResponse()
                                                .writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes())))
                                                .then(Mono.empty()));
                            } else {
                                return Mono.error(e);
                            }
                        });
            }
            return chain.filter(exchange);
        };
    }

    private Mono<String> serializeResponseToJSON(Object responseEntity, ObjectMapper objectMapper, ServerWebExchange exchange) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(responseEntity);
            return Mono.just(jsonResponse);
        } catch (JsonProcessingException e) {
            // Handle the exception
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            APIResponse<String> errorResponse = APIResponse.error("JSON processing error");
            try {
                String errorJsonResponse = objectMapper.writeValueAsString(errorResponse);
                return Mono.just(errorJsonResponse);
            } catch (JsonProcessingException ex) {
                return Mono.just("Error generating error response");
            }
        }
    }

    public static class Config {
    }
}
