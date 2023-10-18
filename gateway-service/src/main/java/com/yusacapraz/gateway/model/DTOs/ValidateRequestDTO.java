package com.yusacapraz.gateway.model.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ValidateRequestDTO {
    private String token;
    private String requestPath;
}