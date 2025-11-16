package com.backend.sigclc.Model.ComentariosForos;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RedactorModel {
    private ObjectId usuarioId;
    private String nombreCompleto;

    public String getUsuarioIdAsString(){
        return usuarioId.toHexString();
    }
}

