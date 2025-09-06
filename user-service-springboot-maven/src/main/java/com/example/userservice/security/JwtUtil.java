package com.example.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationSeconds:3600}")
    private long expirationSeconds;

    public String generateToken(String email, UUID uid) {
        if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email no puede ser nulo");
        if (uid == null) throw new IllegalArgumentException("UID no puede ser nulo");

        Instant now = Instant.now();
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        return Jwts.builder()
                .claim("uid", uid.toString())
                .setSubject(email)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(SignatureAlgorithm.HS256, keyBytes)
                .compact();
    }


    public Claims parse(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
