package com.example.userservice.exception;

import com.example.userservice.dto.exception.ErrorEnvelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la aplicación.
 *
 * <p>Intercepta excepciones lanzadas por los controladores y devuelve
 * respuestas HTTP estructuradas mediante {@link ErrorEnvelope}.</p>
 *
 * <p>Se encarga de:</p>
 * <ul>
 *   <li>Manejar validaciones de DTOs con {@link MethodArgumentNotValidException}
 *       devolviendo {@code 400 Bad Request} con detalles del error</li>
 *   <li>Manejar cualquier otra excepción no controlada devolviendo
 *       {@code 500 Internal Server Error}</li>
 * </ul>
 *
 * <p>La anotación {@code @ControllerAdvice} permite aplicar estas reglas
 * globalmente a todos los controladores REST.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de validación de argumentos en los controladores.
     *
     * @param ex excepción lanzada por validación de DTO
     * @return {@link ResponseEntity} con {@link ErrorEnvelope} y código 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEnvelope> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Solicitud inválida");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorEnvelope.of(HttpStatus.BAD_REQUEST.value(), detail));
    }

    /**
     * Maneja cualquier otra excepción no controlada.
     *
     * @param ex excepción capturada
     * @return {@link ResponseEntity} con {@link ErrorEnvelope} y código 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEnvelope> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorEnvelope.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno"));
    }
}
