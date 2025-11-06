package com.backend.sigclc.DTO.Reuniones;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistentesResponseDTO {
    private ObjectId asistenteId;
    private String nombreCompleto;
}
