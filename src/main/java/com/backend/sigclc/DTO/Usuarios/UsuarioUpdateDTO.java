package com.backend.sigclc.DTO.Usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {
    private String nombreCompleto;
    private Integer edad;
    private String ocupacion;
    private String correoElectronico;
    private String telefono;
}
