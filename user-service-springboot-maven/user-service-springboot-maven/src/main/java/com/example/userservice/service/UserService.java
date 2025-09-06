package com.example.userservice.service;

import com.example.userservice.domain.Phone;
import com.example.userservice.domain.User;
import com.example.userservice.dto.PhoneDto;
import com.example.userservice.dto.SignUpRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        Instant now = Instant.now();
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreated(now);
        user.setLastLogin(now);
        user.setIsActive(true);

        if (request.getPhones() != null) {
            List<Phone> phones = request.getPhones().stream().map(p -> {
                Phone ph = Phone.builder()
                        .number(p.getNumber())
                        .citycode(p.getCitycode())
                        .contrycode(p.getContrycode())
                        .user(user)
                        .build();
                return ph;
            }).collect(Collectors.toList());
            user.getPhones().addAll(phones);
        }

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return toResponse(user, token, true);
    }

    @Transactional
    public UserResponse login(String bearerToken) {
        String token = extract(bearerToken);
        var claims = jwtUtil.parse(token);
        String email = claims.getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("usuario no encontrado"));

        user.setLastLogin(Instant.now());
        String newToken = jwtUtil.generateToken(user.getEmail(), user.getId());
        return toResponse(user, newToken, false);
    }

    private String extract(String bearer) {
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization Bearer token requerido");
        }
        return bearer.substring(7);
    }

    private UserResponse toResponse(User u, String token, boolean maskPassword) {
        return UserResponse.builder()
                .id(u.getId())
                .created(u.getCreated())
                .lastLogin(u.getLastLogin())
                .token(token)
                .isActive(Boolean.TRUE.equals(u.getIsActive()))
                .name(u.getName())
                .email(u.getEmail())
                .password(maskPassword ? "*****" : u.getPassword())
                .phones(u.getPhones().stream().map(p -> PhoneDto.builder()
                        .number(p.getNumber()).citycode(p.getCitycode()).contrycode(p.getContrycode()).build()
                ).collect(Collectors.toList()))
                .build();
    }
}
