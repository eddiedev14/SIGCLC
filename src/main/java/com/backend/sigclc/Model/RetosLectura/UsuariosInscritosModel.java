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
public class UsuariosInscritosModel {
    private ObjectId usuarioId;
    private String nombreCompleto;
    private List<ProgresoModel> progreso = new ArrayList<>();

    public String getUsuarioIdAString(){
        return usuarioId != null ? usuarioId.toHexString():null;
    }
}
