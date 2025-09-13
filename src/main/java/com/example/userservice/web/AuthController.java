package com.example.userservice.web;

import com.example.userservice.dto.ErrorEnvelope;
import com.example.userservice.dto.SignUpRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controlador REST encargado de la autenticación y registro de usuarios.
 *
 * <p>Exponen los endpoints:</p>
 * <ul>
 *   <li>{@code POST /sign-up} para registrar nuevos usuarios</li>
 *   <li>{@code GET /login} para iniciar sesión y obtener un token JWT</li>
 * </ul>
 *
 * <p>Usa {@link UserService} para la lógica de negocio y maneja
 * errores devolviendo {@link ErrorEnvelope} con el código HTTP adecuado.</p>
 *
 * <p>La anotación {@code @RestController} indica que esta clase expone
 * endpoints REST y {@code @Validated} permite la validación automática
 * de los DTOs.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * <p>Consume y produce JSON. Valida el DTO {@link SignUpRequest}.
     * Retorna {@link UserResponse} con el token generado en caso de éxito.</p>
     *
     * <p>Códigos de respuesta:</p>
     * <ul>
     *   <li>{@code 201 Created} si el usuario se creó correctamente</li>
     *   <li>{@code 409 Conflict} si ya existe un usuario con el mismo email</li>
     * </ul>
     *
     * @param request DTO con los datos de registro del usuario
     * @return {@link ResponseEntity} con {@link UserResponse} o {@link ErrorEnvelope}
     */
    @PostMapping(value = "/sign-up", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        try {
            UserResponse resp = userService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ErrorEnvelope.of(HttpStatus.CONFLICT.value(), e.getMessage()));
        }
    }

    /**
     * Endpoint para iniciar sesión de un usuario.
     *
     * <p>Se espera un header {@code Authorization} con el token Bearer.
     * Retorna {@link UserResponse} con un nuevo token JWT.</p>
     *
     * <p>Códigos de respuesta:</p>
     * <ul>
     *   <li>{@code 200 OK} si el login es exitoso</li>
     *   <li>{@code 401 Unauthorized} si el token es inválido o el usuario no existe</li>
     * </ul>
     *
     * @param authorization token Bearer enviado en el header Authorization
     * @return {@link ResponseEntity} con {@link UserResponse} o {@link ErrorEnvelope}
     */
    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<?> login(@RequestHeader(name = "Authorization", required = false) String authorization) {
        try {
            UserResponse resp = userService.login(authorization);
            return ResponseEntity.ok(resp);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorEnvelope.of(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
        }
    }
}
