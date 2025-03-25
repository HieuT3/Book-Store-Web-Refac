package com.bookstore.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Getter
public class JwtAuthenticationProvider {

    @Value("${JWT_SECRETKEY}")
    private String secretKey;

    @Value("${JWT_ACCESS_TOKEN_EXPIRATION}")
    private long jwtAccessTokenExpiration;

    @Value("${JWT_REFRESH_TOKEN_EXPIRATION}")
    private long jwtRefreshTokenExpiration;

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("type", "access");
        extractClaims.put("role", userDetails.getAuthorities());
        return generateToken(extractClaims, userDetails, jwtAccessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> extractClaims = new HashMap<>();
        extractClaims.put("type", "refresh");
        return generateToken(extractClaims, userDetails, jwtRefreshTokenExpiration);
    }

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration) {
        return buildToken(extractClaims, userDetails, expiration);
    }

    public String buildToken(Map<String, Object> extractClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuer("book-store-app")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid JWT token");
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = userDetails.getUsername();
        return !isTokenExpired(token) && extractUserName(token).equalsIgnoreCase(username);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }
}
