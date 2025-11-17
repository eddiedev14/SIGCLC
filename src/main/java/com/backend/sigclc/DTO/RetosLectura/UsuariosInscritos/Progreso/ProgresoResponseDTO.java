package com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoResponseDTO {
    private String libroAsociadoId;
    private List<ProgresoLibroResponseDTO> progresoLibro;
}
