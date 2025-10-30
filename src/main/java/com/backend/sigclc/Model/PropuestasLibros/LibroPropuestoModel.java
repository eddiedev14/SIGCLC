package com.backend.sigclc.Model.PropuestasLibros;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroPropuestoModel {
    private ObjectId libroId;
    private String titulo;
    private List<GeneroLibro> generos;
    private Date fechaSeleccion;
    private EstadoLectura estadoLectura;

    public String getLibroPropuestoIdAsString(){
        return libroId.toHexString();
    }
}
