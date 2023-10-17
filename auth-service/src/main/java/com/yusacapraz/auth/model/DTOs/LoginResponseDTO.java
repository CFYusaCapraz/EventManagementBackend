package com.yusacapraz.auth.model.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
}
