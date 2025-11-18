package com.backend.sigclc.DTO.RetosLectura.UsuariosInscritos.Progreso;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoLibroResponseDTO {
    private Date fecha;
    private String observacion;
}
