package com.backend.sigclc.DTO.Resenias.LibroReseniado;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroReseniadoResponseDTO {
    private String libroId;
    private String titulo;
    private Double calificacion;
    private String opinion;
}
