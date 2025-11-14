package com.backend.sigclc.DTO.Reuniones;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenteDTO {
    @NotNull(message = "El id del asistente es obligatorio")
    private ObjectId asistenteId;
}
