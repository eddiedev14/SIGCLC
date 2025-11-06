package com.backend.sigclc.DTO.Reuniones;

import java.util.List;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrosSeleccionadosResponseDTO {
    private String propuestaId;
    private String titulo;
    private List<GeneroLibro> generos;
}
