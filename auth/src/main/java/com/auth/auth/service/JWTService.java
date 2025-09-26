package com.auth.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    // 6️⃣ JWTService — Handles JWT generation, validation, and extraction
    // Called by: JwtAuthenticationFilter, Controllers, or any service needing JWT operations
    // Purpose: Create tokens, extract username/email, validate tokens, and check expiration

    @Value("${security.jwt.secret-key}")
    private String secretKey; // Injected from application.properties — used to sign tokens

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration; // Token expiration in ms

    // Takes: JWT token string
    // Returns: username/email stored in token (subject)
    // Called by: JwtAuthenticationFilter, Controllers, or other services
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Takes: UserDetails (username, password, authorities)
    // Returns: JWT token string
    // Called by: Authentication controller after successful login
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Takes: JWT token string, UserDetails object
    // Returns: true if token is valid and matches user, false otherwise
    // Called by: JwtAuthenticationFilter to validate JWT
    public boolean isValidToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Takes: none
    // Returns: long expiration time in ms
    // Called by: Controllers or services needing expiration info
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // Takes: extra claims map, UserDetails, expiration time
    // Returns: JWT token string
    // Called internally to build the JWT
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // usually email/username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Takes: JWT token string, a claim resolver function
    // Returns: Value of a specific claim
    // Called internally to extract any claim from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Takes: extra claims map, UserDetails
    // Returns: JWT token string using default expiration
    // Called internally by generateToken(UserDetails)
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Takes: JWT token string
    // Returns: true if token is expired, false otherwise
    // Called internally by isValidToken()
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Takes: JWT token string
    // Returns: Expiration date of token
    // Called internally by isTokenExpired()
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Takes: JWT token string
    // Returns: Claims object containing all info stored in token
    // Called internally by extractClaim()
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Takes: none
    // Returns: Signing key used for HS256 JWT
    // Called internally by buildToken() and extractAllClaims()
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
