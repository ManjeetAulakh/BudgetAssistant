package com.budgetassistant.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JwtUtil {

    // Secret key for signing the token
    private static final String SECRET = "my_secure_key_which_is_long_enough_123456";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Token validity (e.g., 5 hours)
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;

    // Generate token based on username
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public String doGenerateToken(Map<String, Object> claims, String username) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    // Extract username from token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract Expiration Date from token
    public Date getExpireationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Check if token is expired
    public boolean isTokenExpired(String token) {
        final Date expiration = getExpireationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Validate token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String extractedUsername = getUsernameFromToken(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Generic method to get any claim from the token
    public <T> T getClaimFromToken(String token, java.util.function.Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
    }

    // Get all claims
    private Claims getAllClaimsFromToken(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}
