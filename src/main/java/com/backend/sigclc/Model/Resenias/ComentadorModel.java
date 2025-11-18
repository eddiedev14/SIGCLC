package com.backend.sigclc.Model.Resenias;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentadorModel {
    private ObjectId usuarioId;
    private String nombreCompleto;

    public String getComentadorIdAString(){
        return usuarioId != null ? usuarioId.toHexString():null;
    }
}
