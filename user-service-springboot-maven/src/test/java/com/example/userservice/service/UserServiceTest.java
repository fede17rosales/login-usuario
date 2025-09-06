package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.dto.SignUpRequest;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateUser_whenEmailNotExists() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(false);
        when(jwtUtil.generateToken(anyString(), any())).thenReturn("jwt-token");

        SignUpRequest req = SignUpRequest.builder()
                .email("a@b.com")
                .password("a2asfGfdfdf4")
                .build();

        var resp = service.signUp(req);

        assertEquals("a@b.com", resp.getEmail());
        assertEquals("jwt-token", resp.getToken());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertTrue(new BCryptPasswordEncoder().matches(
                "a2asfGfdfdf4",
                captor.getValue().getPassword()
        ));
    }

    @Test
    void shouldThrowConflict_whenEmailExists() {
        when(userRepository.existsByEmail("a@b.com")).thenReturn(true);

        SignUpRequest req = SignUpRequest.builder()
                .email("a@b.com")
                .password("a2asfGfdfdf4")
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.signUp(req));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldReturnUserAndRefreshToken_onLogin() {
        var uid = UUID.randomUUID();
        User u = new User();
        u.setId(uid);
        u.setEmail("a@b.com");

        when(userRepository.findByEmail(u.getEmail())).thenReturn(Optional.of(u));

        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("a@b.com");
        when(jwtUtil.parse("abc")).thenReturn(claims);
        when(jwtUtil.generateToken("a@b.com", uid)).thenReturn("new-token");

        var resp = service.login("Bearer abc");

        assertEquals("new-token", resp.getToken());
        assertEquals("a@b.com", resp.getEmail());
    }

    @Test
    void shouldThrowException_onLoginMissingBearer() {
        assertThrows(IllegalArgumentException.class, () -> service.login(null));
        assertThrows(IllegalArgumentException.class, () -> service.login("Token abc"));
    }
}
