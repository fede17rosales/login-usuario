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

/**
 * Servicio encargado de la gestión de usuarios.
 *
 * <p>Provee operaciones como:</p>
 * <ul>
 *   <li>Registro de nuevos usuarios ({@link #signUp(SignUpRequest)})</li>
 *   <li>Inicio de sesión y generación de tokens JWT ({@link #login(String)})</li>
 * </ul>
 *
 * <p>Se encarga de la encriptación de contraseñas, manejo de JWT y mapeo
 * de entidades {@link User} a DTOs {@link UserResponse}.</p>
 *
 * <p>La anotación {@code @Service} indica que es un bean de servicio de Spring.
 * {@code @RequiredArgsConstructor} genera un constructor para inyección de dependencias finales.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>Valida que no exista otro usuario con el mismo email, encripta
     * la contraseña, crea teléfonos asociados si existen y genera un token JWT.</p>
     *
     * @param request DTO con los datos de registro ({@link SignUpRequest})
     * @return {@link UserResponse} con los datos del usuario y token JWT
     * @throws IllegalArgumentException si ya existe un usuario con el mismo email
     */
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
                        .countrycode(p.getCountrycode())
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

    /**
     * Inicia sesión de un usuario usando un token Bearer.
     *
     * <p>Extrae el email del JWT, actualiza el último login y genera
     * un nuevo token JWT.</p>
     *
     * @param bearerToken token JWT recibido en el header Authorization
     * @return {@link UserResponse} con los datos del usuario y nuevo token JWT
     * @throws IllegalArgumentException si el token es inválido o el usuario no existe
     */
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

    /**
     * Extrae el token JWT de un header Authorization tipo Bearer.
     *
     * @param bearer header completo (ej. "Bearer <token>")
     * @return token JWT limpio
     * @throws IllegalArgumentException si el header es nulo o no comienza con "Bearer "
     */
    private String extract(String bearer) {
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization Bearer token requerido");
        }
        return bearer.substring(7);
    }

    /**
     * Mapea una entidad {@link User} a su DTO {@link UserResponse}.
     *
     * <p>Opcionalmente puede enmascarar la contraseña antes de devolverla.</p>
     *
     * @param u entidad {@link User} a mapear
     * @param token token JWT a incluir en la respuesta
     * @param maskPassword {@code true} para enmascarar la contraseña
     * @return {@link UserResponse} con datos del usuario y token JWT
     */
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
                        .number(p.getNumber()).citycode(p.getCitycode()).countrycode(p.getCountrycode()).build()
                ).collect(Collectors.toList()))
                .build();
    }
}
