package com.backend.sigclc.Model.PropuestasLibros;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioProponenteModel {
    private ObjectId usuarioId;
    private String nombreCompleto;
}
