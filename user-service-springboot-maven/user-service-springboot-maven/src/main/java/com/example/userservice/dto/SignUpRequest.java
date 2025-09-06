package com.example.userservice.dto;

import lombok.*;
import javax.validation.constraints.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String name;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
             message = "email con formato inválido")
    private String email;

    @NotBlank
    @Size(min = 8, max = 12, message = "password debe tener entre 8 y 12 caracteres")
    @Pattern(
        regexp = "^(?=(?:.*[A-Z]){1})(?!.*[A-Z].*[A-Z])(?=(?:.*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z\\d]{8,12}$",
        message = "password inválida: exactamente 1 mayúscula y 2 dígitos; solo letras y números"
    )
    private String password;

    private List<PhoneDto> phones;
}
