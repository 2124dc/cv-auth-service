package com.app.cv.service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long expiration;

    // Secure key for HS256 algorithm
    private SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("Extracting all claims from " + token);
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Use the secure key instead of the string secret
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Use expiration from properties
                .signWith(key, SignatureAlgorithm.HS256)  // Sign with the secure key
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Overloaded validateToken method used for checking validity without username
    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token);  // If token parsing fails, the token is invalid
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAdminToken(String token) {
        String role = extractClaim(token, Claims::getSubject); // Assuming you store role in the JWT
        return "ADMIN".equalsIgnoreCase(role);
    }
    
    public boolean isOwnerToken(String token) {
        String role = extractClaim(token, Claims::getSubject); // Assuming you store role in the JWT
        return "OWNER".equalsIgnoreCase(role);
    }
}
