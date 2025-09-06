package com.example.userservice.web;

import com.example.userservice.dto.ErrorEnvelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorEnvelope> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(e -> e.getDefaultMessage())
                .orElse("Solicitud inválida");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorEnvelope.of(HttpStatus.BAD_REQUEST.value(), detail));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorEnvelope> handleOther(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorEnvelope.of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error interno"));
    }
}
