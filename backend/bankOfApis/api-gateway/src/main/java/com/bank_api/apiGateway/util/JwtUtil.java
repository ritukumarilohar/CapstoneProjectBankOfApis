package com.bank_api.apiGateway.util;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    public Claims extractAllClaims(String token) {
    	try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
    
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }
    
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        
        return claims.get("userId", Long.class);
    }
    
    public boolean validateToken(String token) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            SecretKey key = io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);
            
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}