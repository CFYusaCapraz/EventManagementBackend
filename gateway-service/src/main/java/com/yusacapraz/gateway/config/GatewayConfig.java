package com.yusacapraz.gateway.config;

import com.yusacapraz.gateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder, AuthenticationFilter authenticationFilter) {
        return routeLocatorBuilder.routes()
                .route("AUTH-SERVICE", r -> r.path("/api/auth/**").uri("lb://AUTH-SERVICE"))
                .route("DISCOVERY-SERVICE", r -> r.path("/hello")
                        .filters(f -> f.filter(authenticationFilter.
                                apply(new AuthenticationFilter.Config())))
                        .uri("lb://DISCOVERY-SERVICE"))
                .build();
    }
}
