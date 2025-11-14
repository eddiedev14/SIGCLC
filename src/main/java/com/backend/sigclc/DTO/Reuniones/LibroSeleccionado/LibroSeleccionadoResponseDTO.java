package com.backend.sigclc.DTO.Reuniones.LibroSeleccionado;

import java.util.List;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroSeleccionadoResponseDTO {
    private String libroSeleccionadoId;
    private String titulo;
    private List<GeneroLibro> generos;
}
