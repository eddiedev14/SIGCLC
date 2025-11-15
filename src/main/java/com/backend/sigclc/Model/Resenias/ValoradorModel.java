package com.backend.sigclc.Model.Resenias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValoradorModel {
    private ObjectId usuarioId;
    private String nombreCompleto;
}
