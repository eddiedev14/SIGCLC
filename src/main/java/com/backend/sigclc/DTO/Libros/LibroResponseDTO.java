package com.backend.sigclc.DTO.Libros;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponseDTO {
    private String id;
    private String titulo;
    private String autor;
    private String genero;
    private Integer anioPublicacion;
    private String sinopsis;
    private String portadaPath;
    private String registrado_por;
}
