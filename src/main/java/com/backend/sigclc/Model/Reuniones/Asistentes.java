package com.backend.sigclc.Model.Reuniones;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistentes {
    private ObjectId asistenteId;
    private String nombreCompleto;
}
