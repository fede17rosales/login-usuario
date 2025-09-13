package com.example.userservice.domain;

import lombok.*;
import javax.persistence.*;

/**
 * Entidad JPA que representa un teléfono asociado a un usuario.
 *
 * <p>Esta clase se mapea a una tabla en la base de datos donde se almacenan
 * los números de teléfono de los usuarios, junto con su código de ciudad
 * y de país.</p>
 *
 * <p>Relaciones:</p>
 * <ul>
 *   <li>Cada teléfono pertenece a un único usuario ({@code @ManyToOne}).</li>
 *   <li>La relación se realiza mediante la columna {@code user_id}.</li>
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {
    /**
     * Identificador único del teléfono (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Número de teléfono sin código de área.
     */
    private Long number;
    /**
     * Código de la ciudad asociado al número.
     */
    private Integer citycode;
    /**
     * Código del país asociado al número.
     * <p><b>Nota:</b> la variable está escrita como {@code contrycode},
     * probablemente debería corregirse a {@code countrycode}.</p>
     */
    private String countrycode;

    /**
     * Usuario al que pertenece este teléfono.
     * <p>Relación muchos-a-uno ({@code @ManyToOne}) hacia {@link User}.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
