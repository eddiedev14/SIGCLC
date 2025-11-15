package com.backend.sigclc.DTO.Resenias.Valoracion;

import org.bson.types.ObjectId;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValoracionCreateDTO {    
    @NotNull(message = "El usuarioId no puede ser nulo")
    private ObjectId usuarioId;

    @NotNull(message = "La valoracion no puede ser nula")
    @Min(value = 1, message = "La valoracion debe ser mayor o igual a 1")
    @Max(value = 5, message = "La valoracion debe ser menor o igual a 5")
    private Double valoracion;
}
