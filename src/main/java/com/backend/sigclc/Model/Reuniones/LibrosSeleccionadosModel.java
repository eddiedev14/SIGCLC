package com.backend.sigclc.Model.Reuniones;

import java.util.List;

import org.bson.types.ObjectId;

import com.backend.sigclc.Model.Libros.GeneroLibro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrosSeleccionadosModel {
    private ObjectId propuestaId;
    private String titulo;
    private List<GeneroLibro> generos;

    public String getPropuestaIdAString(){
        return propuestaId != null ? propuestaId.toHexString():null;
    }
}
