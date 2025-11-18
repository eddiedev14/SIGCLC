package com.backend.sigclc.Model.RetosLectura;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrosAsociadosModel {
    private ObjectId libroId;
    private String titulo;
    private List<GeneroLibro> generos;

    public String getLibroIdAString(){
        return libroId != null ? libroId.toHexString():null;
    }
}
