package com.backend.sigclc.DTO.Estadisticas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneroPopularResponseDTO {
    private String genero;
    private int reuniones;
    private int retos;
    private int resenias;
    private int comentariosResenias;
    private int foros;
    private int comentariosForos;
    private int popularidadTotal;
}
