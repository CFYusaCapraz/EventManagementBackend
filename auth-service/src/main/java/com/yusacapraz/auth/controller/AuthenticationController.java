package com.yusacapraz.auth.controller;

import com.yusacapraz.auth.model.DTOs.LoginRequestDTO;
import com.yusacapraz.auth.model.DTOs.RefreshTokenRequestDTO;
import com.yusacapraz.auth.model.DTOs.ValidateRequestDTO;
import com.yusacapraz.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authenticationService.login(loginRequestDTO);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody ValidateRequestDTO validateRequestDTO) {
        return authenticationService.validateToken(validateRequestDTO);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        return authenticationService.refreshToken(refreshTokenRequestDTO);
    }
}
