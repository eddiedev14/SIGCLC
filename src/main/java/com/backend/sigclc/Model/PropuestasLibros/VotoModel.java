package com.backend.sigclc.Model.PropuestasLibros;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotoModel {
    private ObjectId usuarioId;
    private String nombreCompleto;
    private Date fechaVoto;
}
