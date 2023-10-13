package com.yusacapraz.auth.model.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateDTO {
    private String token;
    private String requestPath;
}
