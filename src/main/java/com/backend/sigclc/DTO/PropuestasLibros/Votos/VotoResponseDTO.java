package com.backend.sigclc.DTO.PropuestasLibros.Votos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoResponseDTO {
    private String usuarioId;
    private String nombreCompleto;
    private Date fechaVoto;
}
