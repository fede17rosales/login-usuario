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

/**
 * Componente que proporciona utilidades para generar y parsear JSON Web Tokens (JWT)
 * utilizados en la autenticación de usuarios.
 *
 * <p>Se encarga de:
 * <ul>
 *   <li>Generar tokens JWT firmados con HS256.</li>
 *   <li>Parsear tokens JWT para extraer claims.</li>
 * </ul>
 *
 * <p>Los valores de {@code secret} y {@code expirationSeconds} se obtienen del
 * archivo de propiedades de la aplicación ({@code application.properties} o {@code application.yml}).</p>
 *
 * <p>Este componente se puede inyectar en servicios que necesiten generar o validar tokens.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Component
public class JwtUtil {

    /** Secreto utilizado para firmar los tokens JWT */
    @Value("${app.jwt.secret}")
    private String secret;

    /** Tiempo de expiración del token en segundos (por defecto 3600s = 1 hora) */
    @Value("${app.jwt.expirationSeconds:3600}")
    private long expirationSeconds;

    /**
     * Genera un token JWT para un usuario.
     *
     * <p>Incluye el UID del usuario como claim y el email como subject.
     * El token se firma usando HS256.</p>
     *
     * @param email correo electrónico del usuario (no puede ser nulo ni vacío)
     * @param uid identificador único del usuario (no puede ser nulo)
     * @return token JWT firmado como {@link String}
     * @throws IllegalArgumentException si {@code email} o {@code uid} son nulos o inválidos
     */
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

    /**
     * Parsea un token JWT y devuelve sus claims.
     *
     * @param token token JWT a parsear
     * @return claims contenidos en el token
     * @throws io.jsonwebtoken.JwtException si el token es inválido o ha expirado
     */
    public Claims parse(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }
}
