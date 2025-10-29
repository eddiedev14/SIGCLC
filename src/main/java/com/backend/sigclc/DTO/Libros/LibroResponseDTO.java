package com.backend.sigclc.DTO.Libros;


import java.util.List;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponseDTO {
    private String id;
    private String titulo;
    private List<String> autores;
    private GeneroLibro genero;
    private Integer anioPublicacion;
    private String sinopsis;
    private String portadaPath;
    private String registrado_por;
}
