package com.backend.sigclc.DTO.Estadisticas;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroLeidoResponseDTO {
    private String id;
    private String titulo;
    private List<String> generos;
    private int lecturasPropuestas;
    private int lecturasRetos;
    private int totalLecturas;
}
