package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;

import java.util.List;

/**
 * Data Transfer Object (DTO) que representa la solicitud de registro de un usuario.
 *
 * <p>Incluye datos personales, credenciales de acceso y teléfonos asociados.
 * Esta clase está diseñada para ser utilizada en la capa de entrada de la API
 * durante el proceso de registro.</p>
 *
 * <p>Gracias a Lombok, esta clase incluye automáticamente:
 * <ul>
 *   <li>Getters y setters para todos los campos</li>
 *   <li>Constructor sin argumentos</li>
 *   <li>Constructor con todos los argumentos</li>
 *   <li>Un patrón builder</li>
 * </ul>
 *
 * <p>Además, utiliza anotaciones de {@code javax.validation.constraints}
 * para validar los valores de los campos antes de procesarlos.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    /**
     * Nombre del usuario.
     */
    @Schema(description = "Nombre completo del usuario", example = "Federico Rosales")
    private String name;

    /**
     * Correo electrónico del usuario.
     * <p>
     * Validaciones aplicadas:
     * <ul>
     *   <li>No puede estar en blanco ({@code @NotBlank})</li>
     *   <li>Debe cumplir el formato estándar de email ({@code @Pattern})</li>
     * </ul>
     * </p>
     */
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
             message = "email con formato inválido")
    @Schema(description = "Correo electrónico del usuario", example = "federico@mail.com")
    private String email;

    /**
     * Contraseña del usuario.
     * <p>
     * Validaciones aplicadas:
     * <ul>
     *   <li>No puede estar en blanco ({@code @NotBlank})</li>
     *   <li>Debe tener entre 8 y 12 caracteres ({@code @Size})</li>
     *   <li>Debe contener exactamente 1 mayúscula</li>
     *   <li>Debe contener exactamente 2 dígitos</li>
     *   <li>Solo puede incluir letras y números ({@code @Pattern})</li>
     * </ul>
     * </p>
     */
    @NotBlank
    @Size(min = 8, max = 12, message = "password debe tener entre 8 y 12 caracteres")
    @Pattern(
        regexp = "^(?=(?:.*[A-Z]){1})(?!.*[A-Z].*[A-Z])(?=(?:.*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z\\d]{8,12}$",
        message = "password inválida: exactamente 1 mayúscula y 2 dígitos; solo letras y números"
    )
    @Schema(description = "Contraseña del usuario", example = "MiContraseña123!")
    private String password;

    /**
     * Lista de teléfonos asociados al usuario.
     */
    @Schema(description = "Lista de teléfonos del usuario")
    private List<PhoneDto> phones;
}
