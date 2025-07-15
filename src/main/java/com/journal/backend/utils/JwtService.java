package com.journal.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;


@Component
public class JwtService {
    private String SECRET_KEY="b6b91f6451d71df9a6d99e4ea88c8d59c5f6d13fd3d6e498e89f1ef7a0c05f91";
    public String generateToken(String username) {
        HashMap<String, Object> map=new HashMap<>();
        return createToken(map, username);
    }

    private String createToken(HashMap<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String jwt, UserDetails userDetails) {
        String username=extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractAllClaims(jwt).getExpiration();
    }
}