package com.backend.sigclc.DTO.Usuarios;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {

    @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor de 120 años")
    private Integer edad;

    @Size(min = 2, max = 50, message = "La ocupación debe tener entre 2 y 50 caracteres")
    private String ocupacion;

    @Email(message = "El correo electrónico no tiene un formato válido.")
    private String correoElectronico;

    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos numéricos.")
    private String telefono;
}
