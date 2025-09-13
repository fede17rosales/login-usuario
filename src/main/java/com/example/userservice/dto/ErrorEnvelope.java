package com.example.userservice.dto;

import lombok.*;
import java.time.Instant;
import java.util.Collections;
import java.util.List;


/**
 * DTO que representa un sobre de errores (Error Envelope) para respuestas de la API.
 *
 * <p>Contiene una lista de {@link ErrorItem} con información detallada de cada error.</p>
 *
 * <p>Se utiliza en los controladores y en el {@link com.example.userservice.web.GlobalExceptionHandler}
 * para devolver errores de manera consistente.</p>
 *
 * Ejemplo de uso:
 * <pre>
 * return ResponseEntity.status(HttpStatus.BAD_REQUEST)
 *                      .body(ErrorEnvelope.of(400, "Solicitud inválida"));
 * </pre>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorEnvelope {

    /**
     * Clase interna que representa un error individual dentro del {@link ErrorEnvelope}.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorItem {
        /** Fecha y hora en que ocurrió el error */
        private Instant timestamp;
        /** Código de estado HTTP o código de error */
        private int codigo;
        /** Mensaje descriptivo del error */
        private String detail;
    }

    /** Lista de errores contenidos en el sobre */
    private List<ErrorItem> error;

    /**
     * Crea un {@link ErrorEnvelope} a partir de un código y un mensaje de detalle.
     *
     * @param code código de error o HTTP
     * @param detail mensaje descriptivo del error
     * @return {@link ErrorEnvelope} con un único {@link ErrorItem}
     */
    public static ErrorEnvelope of(int code, String detail) {
        return new ErrorEnvelope(Collections.singletonList(
                ErrorItem.builder().timestamp(Instant.now()).codigo(code).detail(detail).build()
        ));
    }
}
