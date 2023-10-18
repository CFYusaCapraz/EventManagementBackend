package com.yusacapraz.auth.service;

import com.yusacapraz.auth.model.APIResponse;
import com.yusacapraz.auth.model.DTOs.*;
import com.yusacapraz.auth.model.RefreshToken;
import com.yusacapraz.auth.model.exception.RefreshTokenExpiredException;
import com.yusacapraz.auth.model.exception.RefreshTokenNotFoundException;
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

import java.util.UUID;

@Service
public class AuthenticationService {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<APIResponse<?>> login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );
            String accessToken = jwtTokenProvider.generateTokenWithAuthentication(authentication);
            String refreshToken = refreshTokenService.createRefreshToken(loginRequestDTO.getUsername()).getRefreshToken().toString();

            LoginResponseDTO responseDTO = LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            APIResponse<LoginResponseDTO> response = APIResponse.successWithData(responseDTO, "Successfully logged in.");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (AuthenticationException ex) {
            APIResponse<Object> response = APIResponse.error("Login unsuccessful.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> validateToken(ValidateRequestDTO validateRequestDTO) {
        try {
            if (jwtTokenProvider.validateToken(validateRequestDTO.getToken())) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.isAuthenticated()) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                APIResponse<String> response = APIResponse.success("Token validated.");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.internalServerError().build();
        } catch (ExpiredJwtException e) {
            APIResponse<String> response = APIResponse.error("Expired token! Please use the refresh token to get a new access token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<APIResponse<?>> refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        try {
            String refreshToken = refreshTokenRequestDTO.getRefreshToken();
            RefreshToken refreshTokenByToken = refreshTokenService.findRefreshTokenByToken(UUID.fromString(refreshToken));

            // This method will only be called because if the refresh token is expired
            // RefreshTokenExpiredException exception will be thrown
            refreshTokenService.isRefreshTokenExpired(refreshTokenByToken);

            UUID userId = refreshTokenByToken.getUser().getUserId();
            String accessToken = jwtTokenProvider.generateTokenWithUserId(userId);

            RefreshTokenResponseDTO responseDTO = RefreshTokenResponseDTO.builder()
                    .accessToken(accessToken)
                    .build();
            APIResponse<RefreshTokenResponseDTO> response = APIResponse.successWithData(responseDTO, "New access token created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RefreshTokenNotFoundException e) {
            APIResponse<String> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (RefreshTokenExpiredException e) {
            APIResponse<String> response = APIResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
