package com.backend.sigclc.Model.Resenias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroReseniadoModel {
    private ObjectId libroId;
    private String titulo;
    private Double calificacion;
    private String opinion;
}
