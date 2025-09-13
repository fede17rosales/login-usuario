package com.example.userservice.repository;

import com.example.userservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link User}.
 *
 * <p>Proporciona operaciones CRUD y consultas personalizadas
 * para gestionar usuarios en la base de datos.</p>
 *
 * <p>Extiende {@link JpaRepository}, lo que permite acceder a métodos
 * estándar como {@code save}, {@code findById}, {@code findAll},
 * {@code delete}, entre otros.</p>
 *
 * <p>Incluye además consultas personalizadas:</p>
 * <ul>
 *   <li>{@link #findByEmail(String)}: busca un usuario por su correo electrónico.</li>
 *   <li>{@link #existsByEmail(String)}: verifica si existe un usuario con un correo específico.</li>
 * </ul>
 *
 * @author Federico Rosales
 * @since 1.0
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return un {@link Optional} que contiene el usuario si existe, o vacío si no
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el correo electrónico dado.
     *
     * @param email correo electrónico a verificar
     * @return {@code true} si existe un usuario con ese email, {@code false} en caso contrario
     */
    boolean existsByEmail(String email);
}
