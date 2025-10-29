package com.backend.sigclc.DTO.PropuestasLibros;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    private ObjectId usuarioId;
    private String nombreCompleto;
    private Date fechaVoto;
}
