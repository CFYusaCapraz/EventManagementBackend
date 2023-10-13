package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.DTOs.LoginDTO;
import com.yusacapraz.auth.model.DTOs.ValidateDTO;
import com.yusacapraz.auth.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> validateToken(ValidateDTO validateDTO) {
        try {
            if (jwtTokenProvider.validateToken(validateDTO.getToken())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.isAuthenticated()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

                }
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.internalServerError().build();
        } catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest().body("Expired token!");
        }
    }
}
