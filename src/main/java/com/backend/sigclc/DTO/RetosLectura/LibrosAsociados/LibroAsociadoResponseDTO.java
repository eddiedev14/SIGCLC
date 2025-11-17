package com.backend.sigclc.DTO.RetosLectura.LibrosAsociados;

import java.util.List;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroAsociadoResponseDTO {
    private String libroId;
    private String titulo;
    private List<GeneroLibro> generos;
}
