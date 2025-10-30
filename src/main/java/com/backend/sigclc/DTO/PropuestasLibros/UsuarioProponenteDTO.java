package com.backend.sigclc.DTO.PropuestasLibros;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioProponenteDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private ObjectId usuarioId;
}
