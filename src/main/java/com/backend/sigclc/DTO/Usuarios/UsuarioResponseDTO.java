package com.backend.sigclc.DTO.Usuarios;

import com.backend.sigclc.Model.Usuarios.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private String id;
    private String nombreCompleto;
    private Integer edad;
    private String ocupacion;
    private String correoElectronico;
    private String telefono;
    private RolUsuario rol;
}

