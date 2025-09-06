package com.example.userservice.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @Test
    void generateAndParse_roundtrip() {
        JwtUtil util = new JwtUtil();
        try {
            var f1 = JwtUtil.class.getDeclaredField("secret");
            f1.setAccessible(true);
            f1.set(util, "secret");
            var f2 = JwtUtil.class.getDeclaredField("expirationSeconds");
            f2.setAccessible(true);
            f2.set(util, 3600L);
        } catch (Exception e) {
            fail(e);
        }

        String token = util.generateToken("test@example.com", UUID.randomUUID());
        Claims claims = util.parse(token);
        assertEquals("test@example.com", claims.getSubject());
        assertNotNull(claims.get("uid"));
    }
}
