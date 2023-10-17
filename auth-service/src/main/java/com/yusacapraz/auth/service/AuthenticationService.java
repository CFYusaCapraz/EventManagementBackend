package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.APIResponse;
import com.yusacapraz.auth.model.DTOs.LoginDTO;
import com.yusacapraz.auth.model.DTOs.LoginResponseDTO;
import com.yusacapraz.auth.model.DTOs.ValidateDTO;
import com.yusacapraz.auth.model.DTOs.ValidateResponseDTO;
import com.yusacapraz.auth.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<APIResponse<?>> login(LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );
            String token = jwtTokenProvider.generateToken(authentication);

            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .token(token)
                    .build();
            APIResponse<LoginResponseDTO> response = APIResponse.successWithData(responseDTO, "Successfully logged in.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException ex) {
            APIResponse<Object> response = APIResponse.error("Login unsuccessful.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> validateToken(ValidateDTO validateDTO) {
        try {
            if (jwtTokenProvider.validateToken(validateDTO.getToken())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.isAuthenticated()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                ValidateResponseDTO responseDTO = ValidateResponseDTO.builder()
                        .build();
                APIResponse<ValidateResponseDTO> response = APIResponse.successWithData(responseDTO, "Token validated.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.internalServerError().build();
        } catch (ExpiredJwtException e) {
            APIResponse<Object> response = APIResponse.error("Expired token!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
