package com.yusacapraz.auth.security.jwt;

import com.yusacapraz.auth.security.JDBCUserDetails;
import com.yusacapraz.auth.service.UserService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static com.yusacapraz.auth.util.KeyUtils.loadSecretKeyFromString;

@Component
public class JwtTokenProvider {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserService userService;

    public String generateToken(Authentication authentication) {
        SecretKey secretKey = loadSecretKeyFromString(jwtConfig.getSecret());
        JDBCUserDetails principal = (JDBCUserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationMs());

        return Jwts.builder()
                .subject(principal.user().getUserId().toString())
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    public UUID getUserIdFromJWT(String token) {
        SecretKey secretKey = loadSecretKeyFromString(jwtConfig.getSecret());
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        Jws<Claims> jws = parser.parseSignedClaims(token);
        Claims claims = jws.getPayload();
        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String authToken) throws JwtException {
        SecretKey secretKey = loadSecretKeyFromString(jwtConfig.getSecret());
        Jws<Claims> jws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(authToken);
        return true;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token, HttpServletRequest request) {
        UUID userIdFromJWT = getUserIdFromJWT(token);
        UserDetails userDetails = userService.loadUserByUserId(userIdFromJWT);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }
}
