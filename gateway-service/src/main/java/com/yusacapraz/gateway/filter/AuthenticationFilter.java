package com.yusacapraz.gateway.filter;

import com.yusacapraz.gateway.model.DTOs.ValidateDTO;
import com.yusacapraz.gateway.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator routeValidator;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RestTemplate restTemplate;

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
                ValidateDTO validateDTO = ValidateDTO.builder()
                        .token(token)
                        .requestPath(exchange.getRequest().getPath().toString()).build();
                ResponseEntity<String> response = restTemplate.postForEntity(
                        "http://AUTH-SERVICE/api/auth/validateToken",
                        validateDTO,
                        String.class
                );

            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
