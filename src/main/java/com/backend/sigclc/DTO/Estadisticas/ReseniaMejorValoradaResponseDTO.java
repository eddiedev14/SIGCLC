package com.backend.sigclc.DTO.Estadisticas;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniaMejorValoradaResponseDTO {
    private String nombreRedactor;
    private Date fechaPublicacion;
    private String tituloLibro;
    private String opinion;
    private Double calificacionResenia;
}
