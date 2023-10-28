package com.yusacapraz.gateway.service;

import com.yusacapraz.gateway.model.APIResponse;
import com.yusacapraz.gateway.model.DTOs.ValidateRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GatewayService {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public GatewayService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<ResponseEntity<APIResponse>> authenticateToken(ValidateRequestDTO validateRequestDTO) {
        return webClientBuilder
                .build()
                .post()
                .uri("lb://AUTH-SERVICE/api/auth/validateToken")
                .bodyValue(validateRequestDTO)
                .exchangeToMono(clientResponse -> {
                    return clientResponse.toEntity(APIResponse.class);
                });
    }
}
