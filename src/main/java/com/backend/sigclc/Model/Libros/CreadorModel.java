package com.backend.sigclc.Model.Libros;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreadorModel {
    private ObjectId usuarioId;
    private String nombreCompleto;

    public String getUsuarioIdAsString(){
        return usuarioId.toHexString();
    }
}
