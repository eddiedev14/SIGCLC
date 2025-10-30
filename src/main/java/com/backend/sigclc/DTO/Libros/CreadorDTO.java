package com.backend.sigclc.DTO.Libros;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreadorDTO {
    @NotNull(message = "El id del usuario que registra es obligatorio")
    private ObjectId usuarioId;
}
