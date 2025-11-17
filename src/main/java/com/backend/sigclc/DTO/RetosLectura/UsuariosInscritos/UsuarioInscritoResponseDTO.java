package com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos;

import java.util.List;

import com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso.ProgresoResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioInscritoResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
    private List<ProgresoResponseDTO> progreso;
}
