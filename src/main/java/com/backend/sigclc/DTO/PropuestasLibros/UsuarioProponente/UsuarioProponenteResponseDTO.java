package com.backend.sigclc.DTO.PropuestasLibros.UsuarioProponente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioProponenteResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
}
