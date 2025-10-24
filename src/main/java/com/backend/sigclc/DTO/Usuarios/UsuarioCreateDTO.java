package com.backend.sigclc.DTO.Usuarios;

import com.backend.sigclc.Model.Usuarios.RolUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
    private String nombreCompleto;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor de 120 años")
    private Integer edad;

    @NotBlank(message = "La ocupación es obligatoria")
    @Size(min = 2, max = 50, message = "La ocupación debe tener entre 2 y 50 caracteres")
    private String ocupacion;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    private String correoElectronico;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
    private String telefono;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuario rol;
}
