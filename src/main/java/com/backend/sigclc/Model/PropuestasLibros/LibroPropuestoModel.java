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

    // Por defecto se crea sin fecha de seleccion
    private Date fechaSeleccion = null;

    // Por defecto se crea sin estado de lectura
    private EstadoLectura estadoLectura = null;

    public String getLibroPropuestoIdAsString(){
        return libroId.toHexString();
    }
}
