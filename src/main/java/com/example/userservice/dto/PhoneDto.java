package com.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


/**
 * Data Transfer Object (DTO) que representa un teléfono.
 *
 * <p>Incluye el número, el código de ciudad y el código de país.
 * Se utiliza para transportar la información de un teléfono
 * entre capas de la aplicación.</p>
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
public class PhoneDto {
    /**
     * Número de teléfono sin código de área.
     */
    @Schema(description = "Número de teléfono", example = "12345678")
    private Long number;
    /**
     * Código de la ciudad asociado al número.
     */
    @Schema(description = "Código de ciudad", example = "11")
    private Integer citycode;
    /**
     * Código del país asociado al número.
     */
    @Schema(description = "Código de país", example = "54")
    private String countrycode;
}
