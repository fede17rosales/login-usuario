package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase de configuración de Spring que define beans utilizados en la aplicación.
 *
 * <p>En este caso, se configura un {@link BCryptPasswordEncoder} para
 * manejar la encriptación de contraseñas, asegurando que las credenciales
 * de los usuarios se almacenen de manera segura.</p>
 *
 * <p>La anotación {@code @Configuration} indica que esta clase provee
 * definiciones de beans al contexto de Spring.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Configuration
public class BeansConfig {

    /**
     * Bean encargado de encriptar contraseñas mediante el algoritmo
     * {@link BCryptPasswordEncoder}.
     *
     * <p>Se utiliza en la capa de seguridad para codificar contraseñas
     * antes de persistirlas en la base de datos y para validarlas
     * durante el proceso de autenticación.</p>
     *
     * @return una instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
