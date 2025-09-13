package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) que representa la respuesta enviada al cliente
 * con los datos de un usuario.
 *
 * <p>Incluye información de identificación, credenciales, estado de la cuenta
 * y datos de auditoría como la fecha de creación y el último inicio de sesión.</p>
 *
 * <p>Gracias a Lombok, esta clase incluye automáticamente:
 * <ul>
 *   <li>Getters y setters para todos los campos</li>
 *   <li>Constructor sin argumentos</li>
 *   <li>Constructor con todos los argumentos</li>
 *   <li>Un patrón builder</li>
 * </ul>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    /**
     * Identificador único del usuario.
     */
    @Schema(description = "Identificador único del usuario.")
    private UUID id;

    /**
     * Fecha y hora en la que el usuario fue creado.
     */
    @Schema(description = "Fecha y hora en la que el usuario fue creado.")
    private Instant created;

    /**
     * Fecha y hora del último inicio de sesión del usuario.
     */
    @Schema(description = "Fecha y hora del último inicio de sesión del usuario.")
    private Instant lastLogin;

    /**
     * Token de autenticación generado para el usuario.
     */
    @Schema(description = "Token de autenticación generado para el usuario.")
    private String token;

    /**
     * Indica si la cuenta del usuario está activa.
     */
    @Schema(description = "Indica si la cuenta del usuario está activa.")
    private boolean isActive;

    /**
     * Nombre completo del usuario.
     */
    @Schema(description = "Nombre completo del usuario.")
    private String name;

    /**
     * Correo electrónico del usuario.
     */
    @Schema(description = "Correo electrónico del usuario.")
    private String email;

    /**
     * Contraseña del usuario.
     * <p><b>Nota:</b> Por motivos de seguridad, normalmente este campo no debería
     * exponerse en respuestas públicas de la API.</p>
     */
    @Schema(description = "Contraseña encriptada.")
    private String password;

    /**
     * Lista de teléfonos asociados al usuario.
     */
    @Schema(description = "Lista de teléfonos asociados al usuario.")
    private List<PhoneDto> phones;
}
