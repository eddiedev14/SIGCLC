package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;

import com.backend.sigclc.Model.Libros.GeneroLibro;
import com.backend.sigclc.Model.PropuestasLibros.EstadoLectura;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroPropuestoResponseDTO {
    private String libroId;
    private String titulo;
    private GeneroLibro genero;
    private Date fechaSeleccion;
    private EstadoLectura estadoLectura;
}
