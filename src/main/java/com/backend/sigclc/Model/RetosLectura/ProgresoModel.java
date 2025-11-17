package com.backend.sigclc.Model.RetosLectura;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgresoModel {
    private ObjectId libroAsociadoId;
    private List<ProgresoLibroModel> progresoLibro = new ArrayList<>();

    public String getLibroAsociadoIdAString(){
        return libroAsociadoId != null ? libroAsociadoId.toHexString():null;
    }
}
