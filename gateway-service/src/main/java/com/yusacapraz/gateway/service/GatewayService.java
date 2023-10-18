package com.yusacapraz.gateway.service;

import com.yusacapraz.gateway.model.DTOs.ValidateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GatewayService {
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Integer> authenticateToken(ValidateRequestDTO validateRequestDTO){
        return webClientBuilder
                .build()
                .post()
                .uri("lb://AUTH-SERVICE/api/auth/validateToken")
                .bodyValue(validateRequestDTO)
                .exchangeToMono(clientResponse -> {
                    return clientResponse.toEntity(ResponseEntity.class)
                            .map(response -> response.getStatusCode().value());
                });
    }
}
