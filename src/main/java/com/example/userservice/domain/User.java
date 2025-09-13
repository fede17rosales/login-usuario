package com.example.userservice.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que representa un usuario del sistema.
 *
 * <p>La entidad se mapea a la tabla {@code users} en la base de datos y
 * almacena información de identificación, credenciales, estado de la cuenta
 * y teléfonos asociados.</p>
 *
 * <p>Características principales:</p>
 * <ul>
 *   <li>El identificador único se genera mediante un {@link UUID}
 *       usando el generador {@code uuid2} de Hibernate.</li>
 *   <li>El campo {@code email} es obligatorio y único
 *       ({@code @Column(nullable = false)}, {@code @UniqueConstraint}).</li>
 *   <li>La contraseña se almacena en texto plano aquí, pero debería
 *       persistirse en formato encriptado por seguridad.</li>
 *   <li>Relación uno-a-muchos con {@link Phone}, con borrado en cascada
 *       y eliminación de huérfanos.</li>
 * </ul>
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
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Identificador único del usuario (clave primaria).
     * Generado automáticamente como {@link UUID} con {@code uuid2}.
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Correo electrónico único del usuario.
     * <p>Campo obligatorio en la base de datos.</p>
     */
    @Column(nullable = false)
    private String email;


    /**
     * Contraseña del usuario.
     * <p><b>Nota:</b> Debe almacenarse de forma encriptada
     * (nunca en texto plano en producción).</p>
     */
    @Column(nullable = false)
    private String password;

    /**
     * Fecha y hora de creación del usuario.
     */
    private Instant created;

    /**
     * Fecha y hora del último inicio de sesión del usuario.
     */
    private Instant lastLogin;

    /**
     * Estado de la cuenta: {@code true} si está activa, {@code false} en caso contrario.
     */
    private Boolean isActive;

    /**
     * Teléfonos asociados al usuario.
     * <p>Relación uno-a-muchos ({@code @OneToMany}) hacia {@link Phone},
     * con borrado en cascada y eliminación de huérfanos.</p>
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Phone> phones = new ArrayList<>();
}
