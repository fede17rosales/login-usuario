package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de la aplicación usando Spring Security.
 *
 * <p>Define las reglas de acceso y personaliza el filtro de seguridad
 * que controla las peticiones HTTP.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>Deshabilita la protección CSRF para simplificar el desarrollo.</li>
 *   <li>Permite el acceso sin autenticación a las rutas:
 *       <ul>
 *         <li>{@code /sign-up}</li>
 *         <li>{@code /login}</li>
 *         <li>{@code /h2-console/**} (consola de H2)</li>
 *       </ul>
 *   </li>
 *   <li>Configura cabeceras para permitir el uso de la consola H2
 *       dentro de un iframe ({@code frameOptions().sameOrigin()}).</li>
 *   <li>Actualmente todas las demás peticiones están permitidas
 *       sin autenticación ({@code anyRequest().permitAll()}).</li>
 * </ul>
 *
 * <p>La anotación {@code @EnableWebSecurity} habilita la integración
 * de Spring Security en la aplicación.</p>
 *
 * @author Federico Rosales
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad de Spring Security.
     *
     * <p>Configura reglas de autorización y ajustes de cabeceras
     * para la aplicación.</p>
     *
     * @param http objeto de configuración de seguridad HTTP
     * @return un {@link SecurityFilterChain} con las reglas de seguridad aplicadas
     * @throws Exception si ocurre un error en la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/sign-up", "/login", "/h2-console/**").permitAll()
                .anyRequest().permitAll()
            .and()
                .headers().frameOptions().sameOrigin(); // h2 console
        return http.build();
    }
}
