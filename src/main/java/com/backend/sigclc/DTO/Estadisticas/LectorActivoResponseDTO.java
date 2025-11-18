package com.backend.sigclc.DTO.Estadisticas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectorActivoResponseDTO {
    private String id;
    private String nombreCompleto;
    private int propuestasCreadas;
    private int votosRealizados;
    private int reseniasCreadas;
    private int comentariosResenias;
    private int comentariosForos;
    private int inscripcionesRetos;
    private int actividadTotal;
}