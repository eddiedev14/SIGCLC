package com.backend.sigclc.DTO.PropuestasLibros.LibroPropuesto;

import java.util.Date;
import java.util.List;

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
    private List<GeneroLibro> generos;
    private Date fechaSeleccion;
    private EstadoLectura estadoLectura;
}
