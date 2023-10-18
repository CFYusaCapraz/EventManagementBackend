package com.yusacapraz.gateway.filter;

import com.yusacapraz.gateway.model.DTOs.ValidateRequestDTO;
import com.yusacapraz.gateway.service.GatewayService;
import com.yusacapraz.gateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private GatewayService gatewayService;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Missing authorization header");
                }
                String token = jwtUtils.resolveToken(exchange.getRequest());
                if (token == null) {
                    throw new RuntimeException("Missing `Bearer` prefix in the authorization header value");
                }
                ValidateRequestDTO validateRequestDTO = ValidateRequestDTO.builder()
                        .token(token)
                        .requestPath(exchange.getRequest().getPath().toString()).build();

                return gatewayService.authenticateToken(validateRequestDTO)
                        .flatMap(statusCode -> {
                            if (statusCode == HttpStatus.OK.value()) {
                                return chain.filter(exchange);
                            } else {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }
                        })
                        .onErrorResume(WebClientResponseException.class, e -> {
                            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            } else {
                                return Mono.error(e);
                            }
                        });
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
