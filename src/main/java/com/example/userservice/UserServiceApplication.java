package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación UserService.
 *
 * <p>Se encarga de iniciar el contexto de Spring Boot y levantar
 * el servidor embebido para exponer los endpoints de la API.</p>
 *
 * <p>La anotación {@code @SpringBootApplication} habilita:</p>
 * <ul>
 *   <li>Configuración automática de Spring Boot</li>
 *   <li>Escaneo de componentes en el paquete {@code com.example.userservice} y subpaquetes</li>
 *   <li>Definición de beans y configuración base de la aplicación</li>
 * </ul>
 *
 * <p>El método {@code main} ejecuta la aplicación.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@SpringBootApplication
public class UserServiceApplication {
    /**
     * Punto de entrada de la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
